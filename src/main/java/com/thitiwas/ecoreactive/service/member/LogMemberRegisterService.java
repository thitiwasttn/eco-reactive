package com.thitiwas.ecoreactive.service.member;

import com.thitiwas.ecoreactive.entity.report.LogMemberRegisterEntity;
import com.thitiwas.ecoreactive.repository.LogMemberRegisterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Service
public class LogMemberRegisterService {
    private final LogMemberRegisterRepository logMemberRegisterRepository;

    public LogMemberRegisterService(LogMemberRegisterRepository logMemberRegisterRepository) {
        this.logMemberRegisterRepository = logMemberRegisterRepository;
    }

    public Mono<Void> saveLogRegister(Long memberId, String deviceOs) {
        return Mono.defer(() -> logMemberRegisterRepository.findByMemberId(memberId))
                .defaultIfEmpty(new LogMemberRegisterEntity())
                .flatMap(entity -> {
                    LocalDateTime current = LocalDateTime.now();
                    entity.setMemberId(memberId);
                    entity.setRegisterDate(current);
                    entity.setDeviceOS(deviceOs);
                    entity.setCreateDate(current);
                    entity.setCreateBy(0L);
                    entity.setUpdateDate(current);
                    entity.setUpdateBy(0L);
                    return logMemberRegisterRepository.save(entity);
                }).then();
    }
}
