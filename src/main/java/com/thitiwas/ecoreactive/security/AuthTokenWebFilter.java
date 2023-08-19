
package com.thitiwas.ecoreactive.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;


@Slf4j
@RequiredArgsConstructor
public class AuthTokenWebFilter implements WebFilter {

    private String claimId = "id";

    private final AuthTokenService authTokenService;

    private final DefaultUserDetailsJwtClaimsConverter defaultUserDetailsJwtClaimsConverter;

    private final ServerSecurityContextRepository securityContextRepository;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final String authorization = request.getHeaders().getFirst("Authorization");
        final String token = parseToken(authorization);
        if (hasText(token)) {
            return authTokenService.verifyV2(token)
                    .flatMap(claims -> {
                        log.info("claims :{}", claims);
                        return securityContextRepository.save(exchange, toSecurityContextV2(claims));
                    })
                    .then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }



    private SecurityContext toSecurityContext(final Map<String, Object> claims) {
        final DefaultUserDetails userDetails = defaultUserDetailsJwtClaimsConverter.convert(claims);
        final SecurityContext securityContext = new SecurityContextImpl();
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(userDetails);
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    private SecurityContext toSecurityContextV2(DecodedJWT claims) {
        /*String userType = claims.getClaim(claimId).asString();
        // log.debug("userType :{}", userType);
        final SecurityContext securityContext = new SecurityContextImpl();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userType));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        claimId,
                        claims.getToken(),
                        grantedAuthorities);
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        return securityContext;*/

        /*final DefaultUserDetails userDetails =
                DefaultUserDetails.builder()
                        .id(claims.getClaim("id").asString())
                        .build();
        final SecurityContext securityContext = new SecurityContextImpl();
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                new ArrayList<>()
        );
        securityContext.setAuthentication(authentication);*/



        final DefaultUserDetails userDetails = DefaultUserDetails.builder()
                .id(claims.getClaim("id").asString())
                .build();
        log.info("userDetails :{}", userDetails.getUsername());
        final SecurityContext securityContext = new SecurityContextImpl();
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                claims,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(userDetails);
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    private String parseToken(final String authorization) {
        if (!hasText(authorization)) {
            return null;
        }
        final String[] arr = authorization.replaceAll("\\s+", " ").trim().split("\\s");
        if (!(arr != null && arr.length == 2)) {
            throw new AuthenticationException("Invalid token");
        }
        if (!"bearer".equalsIgnoreCase(arr[0])) {
            throw new AuthenticationException("Invalid bearer token");
        }
        return arr[1];
    }
}
