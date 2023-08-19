package com.thitiwas.ecoreactive.controller;

import com.thitiwas.ecoreactive.entity.MemberEntity;
import com.thitiwas.ecoreactive.model.CommonConstant;
import com.thitiwas.ecoreactive.model.ResponseWrapper;
import com.thitiwas.ecoreactive.model.member.RequestLogin;
import com.thitiwas.ecoreactive.model.member.ResponseLogin;
import com.thitiwas.ecoreactive.repository.MemberRepository;
import com.thitiwas.ecoreactive.service.ResponseService;
import com.thitiwas.ecoreactive.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ResponseService responseService;

    @Autowired
    public MemberController(MemberService memberService, MemberRepository memberRepository, ResponseService responseService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.responseService = responseService;
    }

    @GetMapping("/member/test")
    public Mono<MemberEntity> test() {

        Mono<MemberEntity> byId = memberRepository.findById(2L);
        return byId.flatMap(memberEntity -> {
            return memberRepository.save(memberEntity);
        });
    }

    @PostMapping("/member/login")
    @Transactional
    public Mono<ResponseWrapper<ResponseLogin>> memberLogin(@RequestBody RequestLogin login) {
        return memberService.login(login).flatMap(responseService::createResponseSuccess);
    }

}
