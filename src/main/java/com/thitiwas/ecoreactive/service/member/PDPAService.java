package com.thitiwas.ecoreactive.service.member;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PDPAService {
    public Mono<Void> acceptLastPDPAWithMemberId(Long id) {
        // TODO
        return Mono.empty();
    }
}
