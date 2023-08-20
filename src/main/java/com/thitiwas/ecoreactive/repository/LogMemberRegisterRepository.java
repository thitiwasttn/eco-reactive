package com.thitiwas.ecoreactive.repository;

import com.thitiwas.ecoreactive.entity.report.LogMemberRegisterEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LogMemberRegisterRepository extends ReactiveCrudRepository<LogMemberRegisterEntity, Long> {
    Mono<LogMemberRegisterEntity> findByMemberId(Long memberId);
}
