package com.thitiwas.ecoreactive.entity.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Table("log_member_register")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogMemberRegisterEntity {

    @Id
    private Long id;

    @Column("member_id")
    private Long memberId;

    @Column("register_date")
    private LocalDateTime registerDate;

    @Column("device_os")
    private String deviceOS;


    @Column("create_date")
    private LocalDateTime createDate;

    @Column("create_by")
    private Long createBy;

    @Column("update_date")
    private LocalDateTime updateDate;

    @Column("update_by")
    private Long updateBy;
}
