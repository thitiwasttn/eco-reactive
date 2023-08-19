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

    boolean validateEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    /**
     * login with email password to auth service
     */
    public Mono<ResponseLogin> login(RequestLogin login) {
        if (!validateEmail(login.getEmail())) {
            throw errorService.emailNotValid();
        }

        Mono<String> accessToken = userService.login(login.getEmail(), login.getPassword());

        Mono<UserEntity> userEntity = userRepository.findByEmailAndType(login.getEmail(), Constant.MEMBER_TYPE)
                .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                .flatMap(userEntity1 -> userEntity1.isDelete() ? Mono.error(errorService.unAuthorized()) : Mono.just(userEntity1));

        /*Mono<MemberEntity> member = userEntity.flatMap(userEntity1 -> memberRepository.findByUserId(userEntity1.getId())
                .switchIfEmpty(Mono.error(errorService::createUserNotFound)))
                .flatMap(memberEntity -> {
                    if (memberEntity.isDelete()) {
                        log.info("member is deleted");
                        return Mono.error(errorService.unAuthorized());
                    } else {
                        return Mono.just(memberEntity);
                    }
                }).flatMap(memberEntity -> {
                    memberEntity.setDeviceOs(login.getDeviceOS());
                    memberEntity.setClientVersion(login.getClientVersion());
                    return memberRepository.save(memberEntity);
                });
*/


        return accessToken.flatMap(token -> {
            return userEntity.switchIfEmpty(Mono.error(errorService::createUserNotFound))
                    .flatMap(userEntity1 -> userEntity1.isDelete() ? Mono.error(errorService.unAuthorized()) : Mono.just(userEntity1))
                    .flatMap(userEntity1 -> {
                        return formatTelnoTo10Digit(userEntity1.getTelno()).flatMap(telnoFormated -> {
                            return memberRepository.findByUserId(userEntity1.getId())
                                    .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                                    .flatMap(memberEntity -> {
                                        if (memberEntity.isDelete()) {
                                            log.info("member is deleted");
                                            return Mono.error(errorService.unAuthorized());
                                        } else {
                                            return Mono.just(memberEntity);
                                        }
                                    }).flatMap(memberEntity -> {
                                        memberEntity.setDeviceOs(login.getDeviceOS());
                                        memberEntity.setClientVersion(login.getClientVersion());
                                        return memberRepository.save(memberEntity);
                                    }).flatMap(memberEntity -> {
                                        return Mono.just(ResponseLogin
                                                .builder()
                                                .memberId(String.valueOf(memberEntity.getId()))
                                                .email(userEntity1.getEmail())
                                                .accessToken(token)
                                                .firstName(memberEntity.getFirstName())
                                                .lastName(memberEntity.getLastName())
                                                .telno(telnoFormated)
                                                .build());
                                    });
                        });
                    });
        });
    }

    Mono<String> formatTelnoTo10Digit(String telno) {
        if (telno != null && !"".equals(telno)) {
            String chkTelno = telno.substring(0, 2);
            if (chkTelno.equals("66")) {
                telno = "0" + telno.substring(2);
            }
        }
        return Mono.justOrEmpty(telno);
    }
}
