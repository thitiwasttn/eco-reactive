package com.thitiwas.ecoreactive.service.member;

import com.thitiwas.ecoreactive.entity.MemberEntity;
import com.thitiwas.ecoreactive.entity.UserEntity;
import com.thitiwas.ecoreactive.model.member.RequestLogin;
import com.thitiwas.ecoreactive.model.member.ResponseLogin;
import com.thitiwas.ecoreactive.repository.MemberRepository;
import com.thitiwas.ecoreactive.repository.UserRepository;
import com.thitiwas.ecoreactive.service.user.UserService;
import com.thitiwas.ecoreactive.service.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ErrorService errorService;

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, ErrorService errorService, UserService userService, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.errorService = errorService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Mono<Boolean> validateEmail(String emailAddress) {
        log.info("validateEmail::{}", emailAddress);
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Mono.fromCallable(() -> Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches());
    }

    Mono<Boolean> validateTelno(String telno) {
        return Mono.fromCallable(() -> telno != null && telno.length() == 10);
    }

    /**
     * login with email password to auth service
     */
    public Mono<ResponseLogin> login(RequestLogin login) {
        return validateEmail(login.getEmail())
                .doOnNext(aBoolean -> {
                    if (!aBoolean) {
                        throw errorService.emailNotValid();
                    }
                })
                .flatMap(isValidEmail -> userService.login(login.getEmail(), login.getPassword())
                        .flatMap(token -> userRepository.findByEmailAndType(login.getEmail(), Constant.MEMBER_TYPE)
                                .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                                .doOnNext(this::isUserDelete)
                                .flatMap(userEntity -> memberRepository.findByUserId(userEntity.getId())
                                        .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                                        .doOnNext(this::isMemberDelete)
                                        .flatMap(memberEntity -> {
                                            memberEntity.setDeviceOs(login.getDeviceOS());
                                            memberEntity.setClientVersion(login.getClientVersion());
                                            memberEntity.setUpdateDate(LocalDateTime.now());
                                            return memberRepository.save(memberEntity);
                                        })
                                        .flatMap(memberEntity -> formatTelnoTo10Digit(userEntity.getTelno())
                                                .map(telnoFormated -> ResponseLogin
                                                        .builder()
                                                        .memberId(String.valueOf(memberEntity.getId()))
                                                        .email(userEntity.getEmail())
                                                        .accessToken(token)
                                                        .firstName(memberEntity.getFirstName())
                                                        .lastName(memberEntity.getLastName())
                                                        .telno(telnoFormated)
                                                        .build())
                                        )
                                )
                        )
                );
    }

    public void isUserDelete(UserEntity userEntity) {
        if (userEntity.isDelete()) {
            throw errorService.unAuthorized();
        }
    }

    public void isMemberDelete(MemberEntity memberEntity) {
        if (memberEntity.isDelete()) {
            throw errorService.unAuthorized();
        }
    }

    Mono<String> formatTelnoTo10Digit(String telno) {
        return Mono.fromCallable(() -> {
            String ret = telno;
            if (telno != null && !"".equals(telno)) {
                String chkTelno = telno.substring(0, 2);
                if (chkTelno.equals("66")) {
                    ret = "0" + telno.substring(2);
                }
            }
            return ret;
        });
    }
}
