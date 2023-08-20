package com.thitiwas.ecoreactive.service.user;

import com.thitiwas.ecoreactive.entity.UserEntity;
import com.thitiwas.ecoreactive.model.auth.CreateUserM;
import com.thitiwas.ecoreactive.model.auth.ResponseCreateUser;
import com.thitiwas.ecoreactive.model.member.ResponseRegisterM;
import com.thitiwas.ecoreactive.repository.UserRepository;
import com.thitiwas.ecoreactive.service.TokenService;
import com.thitiwas.ecoreactive.service.member.ErrorService;
import com.thitiwas.ecoreactive.service.util.Constant;
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
        return Mono.defer(() -> userRepository.customFindByEmailAndPasswordAndConfirm(email, password, 1)
                .switchIfEmpty(Mono.error(errorService.invalidEmailOrPassword()))
                // .doOnNext(userEntity -> log.info("xx"))
                .flatMap(userEntity -> tokenService.createToken(userEntity.getId())
                        .flatMap(s -> updateUserToken(userEntity, s)))
                .map(UserEntity::getAccessToken));
    }

    Mono<UserEntity> updateUserToken(UserEntity user, String token) {
        return Mono.defer(() -> {
            user.setAccessToken(token);
            user.setUpdateDate(LocalDateTime.now());
            return userRepository.save(user);
        });
    }

    public Mono<ResponseCreateUser> createUserMember(CreateUserM createUser) {
        log.info("createUser :{}", createUser);
        Mono<UserEntity> userEntityMono = userRepository.findByEmailAndType(createUser.getEmail(), Constant.MEMBER_TYPE)
                .doOnNext(userEntity -> {
                    if (userEntity.getIsConfirm()) {
                        throw errorService.emailIsAlreadyExist();
                    }
                }).defaultIfEmpty(new UserEntity());

        Mono<UserEntity> byTelnoAndType = userRepository.findByTelnoAndType(createUser.getTelno(), Constant.MEMBER_TYPE)
                .doOnNext(userEntity -> {
                    if (userEntity.getIsConfirm()) {
                        throw errorService.telNoIsAlreadyExist();
                    }
                }).defaultIfEmpty(new UserEntity());
        Mono<UserEntity> userEntityMono1 = userRepository.customFindByEmailAndTelnoAndConfirmAndMember(createUser.getEmail(), createUser.getTelno(), 0);

        Mono<ResponseCreateUser> ret = userEntityMono1
                .defaultIfEmpty(new UserEntity())
                .flatMap(user -> {
                    Long updateBy = 0L;
                    user.setEmail(createUser.getEmail());
                    user.setPassword(createUser.getPassword());
                    user.setCreateDate(LocalDateTime.now());
                    user.setCreateBy(updateBy);
                    user.setUpdateDate(LocalDateTime.now());
                    user.setUpdateBy(updateBy);
                    user.setType(createUser.getType());
                    user.setDelete(false);
                    user.setIsConfirm(false);
                    user.setTelno(createUser.getTelno());

                    return userRepository.save(user);
                }).map(userEntity -> {
                    ResponseCreateUser build = ResponseCreateUser
                            .builder()
                            .id(userEntity.getId())
                            .build();
                    log.info("createUserMember:: {}", build);
                    return build;
                });
        return userEntityMono
                .then(byTelnoAndType)
                .flatMap(userEntity -> ret);
    }
}
