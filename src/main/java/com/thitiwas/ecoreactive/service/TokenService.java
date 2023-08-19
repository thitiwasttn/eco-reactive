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
        return Mono.fromCallable(() -> {
            Algorithm algorithmHS = getAlgorithm(secretLogin);
            return JWT.create()
                    .withClaim(claimId, String.valueOf(userId))
                    .withIssuer(issuer)
                    .sign(algorithmHS);
        });
        /*Algorithm algorithmHS = getAlgorithm(secretLogin);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        String token = JWT.create()
                .withClaim(claimId, String.valueOf(userId))
                .withClaim("create_time", String.valueOf(calendar.getTime().getTime()))
                .withIssuer(issuer)
                .sign(algorithmHS);
        return Mono.just(token);*/
    }

    private Algorithm getAlgorithm(String secretLogin) {
        return Algorithm.HMAC256(secretLogin);
    }
}
