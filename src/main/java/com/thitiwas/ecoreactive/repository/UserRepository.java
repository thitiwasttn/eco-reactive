package com.thitiwas.ecoreactive.repository;

import com.thitiwas.ecoreactive.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findByEmailAndPassword(String email, String password);

    @Query(value = "select * from user where email=:email or telno =:telno and is_confirm =:isConfirm and type = 'MEMBER'")
    Mono<UserEntity> customFindByEmailAndTelnoAndConfirmAndMember(@Param("email") String email, @Param("telno") String telno, @Param("isConfirm") int isConfirm);

    Mono<UserEntity> findByEmail(String email);

    Mono<UserEntity> findByEmailAndType(String email, String type);

    Mono<UserEntity> findByTelno(String telno);

    Mono<UserEntity> findByTelnoAndType(String telno, String type);

    @Query(value = "select * from user where email=:email and password =:password and is_confirm=:confirm")
    Mono<UserEntity> customFindByEmailAndPasswordAndConfirm(@Param("email") String email, @Param("password") String password, @Param("confirm") int confirm);

}
