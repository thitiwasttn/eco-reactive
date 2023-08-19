package com.thitiwas.ecoreactive.service.user;

import com.thitiwas.ecoreactive.entity.UserEntity;
import com.thitiwas.ecoreactive.repository.UserRepository;
import com.thitiwas.ecoreactive.service.TokenService;
import com.thitiwas.ecoreactive.service.member.ErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@Slf4j
public class UserService {

    private final ErrorService errorService;

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(ErrorService errorService, UserRepository userRepository, TokenService tokenService) {
        this.errorService = errorService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public Mono<String> login(String email, String password) {
        return userRepository.customFindByEmailAndPasswordAndConfirm(email, password, 1)
                .switchIfEmpty(Mono.error(errorService.invalidEmailOrPassword()))
                .flatMap(userEntity -> tokenService.createToken(userEntity.getId())
                        .flatMap(s -> updateUserToken(userEntity, s)))
                .map(UserEntity::getAccessToken);
    }

    Mono<UserEntity> updateUserToken(UserEntity user, String token) {
        user.setAccessToken(token);
        user.setUpdateDate(LocalDateTime.now());
        return userRepository.save(user);
    }
}
