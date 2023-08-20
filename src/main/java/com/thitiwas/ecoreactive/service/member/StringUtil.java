package com.thitiwas.ecoreactive.service.member;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StringUtil {

    public Mono<String> genOTP() {
        return Mono.fromCallable(() -> RandomStringUtils.random(6, false, true));
    }

    public Mono<String> genRef() {
        return Mono.fromCallable(() -> RandomStringUtils.random(6, true, false));
    }
}
