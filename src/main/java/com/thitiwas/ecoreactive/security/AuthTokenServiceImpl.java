
package com.thitiwas.ecoreactive.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final Algorithm algorithm;
    @Value("${custom.claim.id}")
    private String claimId;

    @Override
    public Mono<String> sign(final Map<String, Object> claims) {
        return Mono.fromCallable(() -> {
            final Instant issuedAt = Instant.now();
            // final Instant expiresAt = issuedAt.plus(Duration.ofMinutes(expiresMinutes));
            return JWT.create()
                    .withIssuedAt(Date.from(issuedAt))
                    // .withExpiresAt(Date.from(expiresAt))
                    .withClaim("data", claims)
                    .sign(algorithm);
        });
    }


    @Override
    public Mono<DecodedJWT> verifyV2(String token) {
        if (!StringUtils.hasText(token)) {
            return Mono.error(new AuthenticationException("Required token"));
        }

        return Mono.fromCallable(() -> {
            try {
                log.info("token :{}", token);
                DecodedJWT verify = JWT.require(algorithm)
                        .build()
                        .verify(token);
                return verify;
            } catch (JWTVerificationException e) {
                log.warn("VerifyToken error => ", e);
                throw new AuthenticationException("Invalid token");
            }
        });
    }

    @Override
    public Mono<Map<String, Object>> verify(final String token) {

        if (!StringUtils.hasText(token)) {
            return Mono.error(new AuthenticationException("Required token"));
        }

        return Mono.fromCallable(() -> {
            try {
                log.info("token :{}", token);
                DecodedJWT verify = JWT.require(algorithm)
                        .build()
                        .verify(token);
                log.info("verify :{}", verify.getClaim(claimId));
                Map<String, Object> map = verify
                        .getClaims()
                        .get(claimId)
                        .asMap();
                log.info("map :{}", map);
                return map;
            } catch (JWTVerificationException e) {
                log.warn("VerifyToken error => ", e);
                throw new AuthenticationException("Invalid token");
            }
        });
    }

    @Override
    public Mono<Long> getClaimId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> (DecodedJWT) authentication.getPrincipal())
                .map(decodedJWT -> {
                    String string = decodedJWT.getClaim(claimId).asString();
                    log.info("string :{}", string);
                    return Long.valueOf(string);
                });
    }
}
