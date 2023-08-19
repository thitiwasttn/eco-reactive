package com.thitiwas.ecoreactive.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${custom.constant.secretLogin}")
    private String secretLogin;

    @Value("${custom.claim.id}")
    private String claimId;
    @Value("${custom.constant.issuer}")
    private String issuer;

    public Mono<String> createToken(Long userId) {
        return getAlgorithm(secretLogin)
                .map(algorithm -> JWT.create()
                        .withClaim(claimId, String.valueOf(userId))
                        .withIssuer(issuer)
                        .sign(algorithm));
    }

    private Mono<Algorithm> getAlgorithm(String secretLogin) {
        return Mono.fromCallable(() -> Algorithm.HMAC256(secretLogin));
    }
}
