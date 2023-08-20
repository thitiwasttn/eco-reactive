package com.thitiwas.ecoreactive.service.member;

import com.thitiwas.ecoreactive.entity.MemberEntity;
import com.thitiwas.ecoreactive.entity.MemberRegisterOTPEntity;
import com.thitiwas.ecoreactive.entity.UserEntity;
import com.thitiwas.ecoreactive.model.auth.CreateUserM;
import com.thitiwas.ecoreactive.model.member.RequestLogin;
import com.thitiwas.ecoreactive.model.member.RequestRegisterM;
import com.thitiwas.ecoreactive.model.member.ResponseLogin;
import com.thitiwas.ecoreactive.model.member.ResponseRegisterM;
import com.thitiwas.ecoreactive.repository.MemberRegisterOTPRepository;
import com.thitiwas.ecoreactive.repository.MemberRepository;
import com.thitiwas.ecoreactive.repository.UserRepository;
import com.thitiwas.ecoreactive.service.user.UserService;
import com.thitiwas.ecoreactive.service.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ErrorService errorService;

    private final UserService userService;
    private final UserRepository userRepository;
    private final PDPAService pdpaService;

    private final MemberRegisterOTPRepository memberRegisterOTPRepository;
    private final StringUtil stringUtil;

    private final LogMemberRegisterService logMemberRegisterService;

    @Autowired
    public MemberService(MemberRepository memberRepository, ErrorService errorService, UserService userService, UserRepository userRepository, PDPAService pdpaService, MemberRegisterOTPRepository memberRegisterOTPRepository, StringUtil stringUtil, LogMemberRegisterService logMemberRegisterService) {
        this.memberRepository = memberRepository;
        this.errorService = errorService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.pdpaService = pdpaService;
        this.memberRegisterOTPRepository = memberRegisterOTPRepository;
        this.stringUtil = stringUtil;
        this.logMemberRegisterService = logMemberRegisterService;
    }

    public Mono<Boolean> validateEmail(String emailAddress) {
        // log.info("validateEmail::{}", emailAddress);
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
                .then(userService.login(login.getEmail(), login.getPassword())
                        .flatMap(token -> userRepository.findByEmailAndType(login.getEmail(), Constant.MEMBER_TYPE)
                                .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                                .doOnNext(this::isUserDelete)
                                .flatMap(userEntity -> Mono.zip(memberRepository.findByUserId(userEntity.getId())
                                                .switchIfEmpty(Mono.error(errorService::createUserNotFound))
                                                .doOnNext(this::isMemberDelete)
                                                .flatMap(memberEntity -> {
                                                    memberEntity.setDeviceOs(login.getDeviceOS());
                                                    memberEntity.setClientVersion(login.getClientVersion());
                                                    memberEntity.setUpdateDate(LocalDateTime.now());
                                                    return memberRepository.save(memberEntity);
                                                }), formatTelnoTo10Digit(userEntity.getTelno()))
                                        .map(x -> ResponseLogin
                                                .builder()
                                                .memberId(String.valueOf(x.getT1().getId()))
                                                .email(userEntity.getEmail())
                                                .accessToken(token)
                                                .firstName(x.getT1().getFirstName())
                                                .lastName(x.getT1().getLastName())
                                                .telno(x.getT2())
                                                .build()
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

    public void isUserDeleteV2(UserEntity userEntity) {
        if (userEntity.isDelete()) {
            throw Objects.requireNonNull(errorService.unAuthorizedV2().block());
        }
    }

    public void isMemberDelete(MemberEntity memberEntity) {
        // log.info("1");
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

    public Mono<ResponseRegisterM> register(RequestRegisterM requestRegisterM) {
        Mono<Boolean> validateEmail = validateEmail(requestRegisterM.getEmail())
                .doOnNext(aBoolean -> {
                    if (!aBoolean) {
                        throw errorService.emailNotValid();
                    }
                });
        Mono<Boolean> validateTelno = validateTelno(requestRegisterM.getTelno())
                .doOnNext(aBoolean -> {
                    if (!aBoolean) {
                        throw errorService.telnoNotValid();
                    }
                });
        Mono<Void> telno = formatTelnoTo10Digit(requestRegisterM.getTelno())
                .doOnNext(requestRegisterM::setTelno).then();

        return Mono.when(validateEmail, validateTelno, telno)
                .then(userRepository.findByEmailAndType(requestRegisterM.getEmail(), Constant.MEMBER_TYPE)
                        .log()
                        .doOnNext(userEntity -> {
                            if (userEntity.getIsConfirm() && !userEntity.isDelete()) {
                                throw errorService.emailIsAlreadyExist();
                            }
                        }).flatMap(userEntity -> {
                            CreateUserM createUserM = new CreateUserM();
                            createUserM.setEmail(requestRegisterM.getEmail());
                            createUserM.setPassword(requestRegisterM.getPassword());
                            createUserM.setTelno(requestRegisterM.getTelno());
                            createUserM.setType("MEMBER");
                            return userService.createUserMember(createUserM).log();
                        }).flatMap(responseCreateUser -> createOrUpdateMember(requestRegisterM, responseCreateUser.getId())
                                .flatMap(member -> {
                                    return pdpaService.acceptLastPDPAWithMemberId(member.getId())
                                            .then(logMemberRegisterService.saveLogRegister(member.getId(), requestRegisterM.getDeviceOS()))
                                            .then(createOrUpdateRegisterOTP(member));
                                    // return Mono.when(voidMono, voidMono1).then(Mono.defer(() -> orUpdateRegisterOTP));
                                }).flatMap(memberRegisterOTPEntity -> sendRegisterOtpMail(requestRegisterM.getEmail(), memberRegisterOTPEntity.getRef(), memberRegisterOTPEntity.getOtp())
                                        .then(Mono.fromCallable(() -> memberRegisterOTPEntity))
                                ).map(memberRegisterOTPEntity -> {
                                    LocalDateTime expireDate = memberRegisterOTPEntity.getExpireDate();
                                    Instant instant = expireDate.atZone(ZoneId.systemDefault()).toInstant();
                                    Date date = Date.from(instant);
                                    long timeExpiredInSecond = (date.getTime() - Calendar.getInstance().getTime().getTime()) / 1000;
                                    log.info("lastxxx");
                                    return ResponseRegisterM.builder()
                                            .ref(memberRegisterOTPEntity.getRef())
                                            .expiredSecond(String.valueOf(timeExpiredInSecond))
                                            .build();
                                })
                        )
                );
    }

    Mono<Void> sendRegisterOtpMail(String email, String ref, String otp) {
        //
        return Mono.empty();
    }

    public Mono<MemberRegisterOTPEntity> createOrUpdateRegisterOTP(MemberEntity member) {
        Mono<MemberRegisterOTPEntity> memberRegisterOTPEntityMono = memberRegisterOTPRepository.findByMemberId(member.getId())
                .defaultIfEmpty(new MemberRegisterOTPEntity());
        return Mono.zip(memberRegisterOTPEntityMono, stringUtil.genOTP(), stringUtil.genRef())
                .flatMap(zip -> {
                    LocalDateTime current = LocalDateTime.now();
                    Calendar expireDate = Calendar.getInstance();
                    expireDate.add(Calendar.MINUTE, 5);
                    LocalDateTime la = expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    MemberRegisterOTPEntity registerOTP = zip.getT1();
                    registerOTP.setMemberId(member.getId());
                    registerOTP.setRef(zip.getT3());
                    registerOTP.setOtp(zip.getT2());
                    registerOTP.setExpireDate(la);
                    registerOTP.setCreateDate(current);
                    registerOTP.setCreateBy(0L);
                    registerOTP.setUpdateDate(current);
                    registerOTP.setUpdateBy(0L);
                    return memberRegisterOTPRepository.save(registerOTP);
                })
                .log("createOrUpdateRegisterOTP::");
    }

    private Mono<MemberEntity> createOrUpdateMember(RequestRegisterM requestRegister, Long userId) {
        return Mono.defer(() -> memberRepository.findByUserId(userId)
                .defaultIfEmpty(new MemberEntity())
                .flatMap(member -> {
                    log.info("createOrUpdateMember:: {}", member);
                    // Date current = Calendar.getInstance().getTime();
                    LocalDateTime current = LocalDateTime.now();
                    if (member.isDelete()) {
                        member = new MemberEntity();
                    }
                    member.setFirstName(requestRegister.getFirstName());
                    member.setLastName(requestRegister.getLastName());
                    member.setBirthDate(requestRegister.getBirthDay());
                    member.setGender(requestRegister.getGender());
                    member.setStatus("A");
                    member.setDelete(false);
                    member.setDeviceOs(requestRegister.getDeviceOS());
                    member.setClientVersion(requestRegister.getClientVersion());
                    member.setCreateDate(current);
                    member.setCreateBy(0L);
                    member.setUpdateDate(current);
                    member.setUpdateBy(0L);
                    member.setUserId(userId);
                    member.setCoin("0");
                    return memberRepository.save(member);
                })
        );
    }
}
