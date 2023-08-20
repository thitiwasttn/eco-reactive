package com.thitiwas.ecoreactive.controller.member;

import com.thitiwas.ecoreactive.model.ResponseWrapper;
import com.thitiwas.ecoreactive.model.member.RequestRegisterM;
import com.thitiwas.ecoreactive.model.member.ResponseRegisterM;
import com.thitiwas.ecoreactive.service.ResponseService;
import com.thitiwas.ecoreactive.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RegisterController {

    private final ResponseService responseService;
    private final MemberService memberService;

    @Autowired
    public RegisterController(ResponseService responseService, MemberService memberService) {
        this.responseService = responseService;
        this.memberService = memberService;
    }

    @PostMapping("/p/member/register")
    // @Transactional
    public Mono<ResponseWrapper<ResponseRegisterM>> register(@RequestBody RequestRegisterM requestRegisterM) {
        Mono<ResponseRegisterM> ret = memberService.register(requestRegisterM);
        return ret.flatMap(responseService::createResponseSuccess);
    }
}
