
package com.thitiwas.ecoreactive.security;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    //TODO
    //Move to application.properties
    @Value("${custom.constant.secretLogin}")
    private String AUTH_TOKEN_SECRET_KEY;

    private final int AUTH_TOKEN_EXPIRES_MINUTES = 60 * 10; //10 Hr

    private final String[] PUBLIC_ACCESS_PATHS = new String[]{
            "/api/v1/member/login",
            "/api/v1/p/**",
            "/"
    };

    private Algorithm getAlgorithm(String secretLogin) {
        return Algorithm.HMAC256(secretLogin);
    }

    @Bean
    public AuthTokenService authTokenService() {
        return new AuthTokenServiceImpl(getAlgorithm(AUTH_TOKEN_SECRET_KEY));
    }


    /*@Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .securityContextRepository(new AuthServerSecurityContextRepository())
                .exceptionHandling().accessDeniedHandler(new AuthServerAccessDeniedHandler())
                .and()
                .logout().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(PUBLIC_ACCESS_PATHS).permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(
                        new AuthTokenWebFilter(
                                authTokenService(),
                                new DefaultUserDetailsJwtClaimsConverterImpl(),
                                new AuthServerSecurityContextRepository()
                        ),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build();



//        http.csrf().disable();
    }*/

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        /*return http
                .securityContextRepository(new AuthServerSecurityContextRepository())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .accessDeniedHandler(new AuthServerAccessDeniedHandler()))
                .logout(logoutSpec -> logoutSpec.disable()
                        .csrf(csrfSpec -> csrfSpec.disable()
                        .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers(PUBLIC_ACCESS_PATHS).permitAll()
                                .anyExchange().authenticated())))
                .addFilterAt(
                        new AuthTokenWebFilter(
                                authTokenService(),
                                new DefaultUserDetailsJwtClaimsConverterImpl(),
                                new AuthServerSecurityContextRepository()
                        ),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build();*/

        return http
                .securityContextRepository(new AuthServerSecurityContextRepository())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.accessDeniedHandler(new AuthServerAccessDeniedHandler()))
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(PUBLIC_ACCESS_PATHS).permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(
                        new AuthTokenWebFilter(
                                authTokenService(),
                                new DefaultUserDetailsJwtClaimsConverterImpl(),
                                new AuthServerSecurityContextRepository()
                        ),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build();


//        http.csrf().disable();
    }


}
