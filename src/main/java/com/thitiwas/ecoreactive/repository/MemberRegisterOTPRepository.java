package com.thitiwas.ecoreactive.repository;

import com.thitiwas.ecoreactive.entity.MemberRegisterOTPEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRegisterOTPRepository extends ReactiveCrudRepository<MemberRegisterOTPEntity, Long> {
    Mono<MemberRegisterOTPEntity> findByMemberId(Long memberId);
}
