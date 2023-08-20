package com.thitiwas.ecoreactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;

@Table("member_register_otp")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterOTPEntity {
    @Id
    private Long id;

    @Column("member_id")
    private Long memberId;

    @Column("ref")
    private String ref;

    @Column("otp")
    private String otp;

    @Column("expire_date")
    private LocalDateTime expireDate;

    @Column("create_date")
    private LocalDateTime createDate;

    @Column("create_by")
    private Long createBy;

    @Column("update_date")
    private LocalDateTime updateDate;

    @Column("update_by")
    private Long updateBy;
}
