
package com.thitiwas.ecoreactive.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import reactor.core.publisher.Mono;

import java.util.Map;


public interface AuthTokenService {

    Mono<String> sign(final Map<String, Object> claims);

    Mono<Map<String, Object>> verify(final String token);
    Mono<DecodedJWT> verifyV2(final String token);

    Mono<Long> getClaimId();

}
