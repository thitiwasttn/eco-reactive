package com.thitiwas.ecoreactive.controller.member;

import com.thitiwas.ecoreactive.entity.MemberRegisterOTPEntity;
import com.thitiwas.ecoreactive.entity.UserEntity;
import com.thitiwas.ecoreactive.model.ResponseWrapper;
import com.thitiwas.ecoreactive.model.member.RequestLogin;
import com.thitiwas.ecoreactive.model.member.RequestResendOTPM;
import com.thitiwas.ecoreactive.model.member.ResponseLogin;
import com.thitiwas.ecoreactive.model.member.ResponseRegisterM;
import com.thitiwas.ecoreactive.repository.MemberRegisterOTPRepository;
import com.thitiwas.ecoreactive.repository.MemberRepository;
import com.thitiwas.ecoreactive.repository.UserRepository;
import com.thitiwas.ecoreactive.service.ResponseService;
import com.thitiwas.ecoreactive.service.member.MemberService;
import com.thitiwas.ecoreactive.service.member.StringUtil;
import com.thitiwas.ecoreactive.service.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ResponseService responseService;
    private final MemberRegisterOTPRepository memberRegisterOTPRepository;
    private final StringUtil stringUtil;
    private final UserRepository userRepository;


    @Autowired
    public MemberController(MemberService memberService, MemberRepository memberRepository, ResponseService responseService, MemberRegisterOTPRepository memberRegisterOTPRepository, StringUtil stringUtil, UserRepository userRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.responseService = responseService;
        this.memberRegisterOTPRepository = memberRegisterOTPRepository;
        this.stringUtil = stringUtil;
        this.userRepository = userRepository;
    }

    @GetMapping("/p/member/test")
    public Mono<ResponseWrapper<Object>> test() {

        return Mono.just("Heelo").flatMap(responseService::createResponseSuccess);
    }

    @PostMapping("/member/login")
    @Transactional
    public Mono<ResponseWrapper<ResponseLogin>> memberLogin(@RequestBody RequestLogin login) {
        return memberService.login(login).flatMap(responseService::createResponseSuccess);
    }

    @PostMapping("/p/member/resend-otp")
    @Transactional
    public Mono<ResponseWrapper<ResponseRegisterM>> resendOTP(@RequestBody RequestResendOTPM resendOTPM) {
        return memberService.resendOTP(resendOTPM).flatMap(responseService::createResponseSuccess);
    }

}
