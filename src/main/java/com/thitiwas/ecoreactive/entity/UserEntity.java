package com.thitiwas.ecoreactive.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Date;
@Table("user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private Long id;

    @Column("email")
    private String email;

    @Column("password")
    @ToString.Exclude
    private String password;

    @Column("access_token")
    @ToString.Exclude
    private String accessToken;

    @Column("login_expired")
    private LocalDateTime LoginExpired;

    @Column("create_date")
    private LocalDateTime createDate;

/*    @Column("create_by")
    private Long createBy;*/

    @Column("update_date")
    private LocalDateTime updateDate;

/*    @Column("update_by")
    private Long updateBy;*/

    @Column("type")
    private String type;

    @Column("is_delete")
    private boolean isDelete;

    @Column("telno")
    private String telno;

    @Column("is_confirm")
    private Boolean isConfirm;
}
