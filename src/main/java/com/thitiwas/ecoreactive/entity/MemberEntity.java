package com.thitiwas.ecoreactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Table("member")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {

    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private Object birthDate;

    @Column("gender")
    private String gender;

    @Column("status")
    private String status;

    @Column("is_delete")
    private boolean isDelete;

    @Column("device_os")
    private String deviceOs;

    @Column("client_version")
    private String clientVersion;

    @Column("create_date")
    private LocalDateTime createDate;

    @Column("create_by")
    private Long createBy;

    @Column("update_date")
    private LocalDateTime updateDate;

    @Column("update_by")
    private Long updateBy;

    @Column("user_id")
    private Long userId;

    @Column("coin")
    private String coin;

    @Column("register_key")
    private String registerKey;
}
