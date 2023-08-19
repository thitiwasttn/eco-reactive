package com.thitiwas.ecoreactive.repository;

import com.thitiwas.ecoreactive.entity.MemberEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<MemberEntity, Long> {
    Mono<MemberEntity> findByUserId(Long userId);
}
