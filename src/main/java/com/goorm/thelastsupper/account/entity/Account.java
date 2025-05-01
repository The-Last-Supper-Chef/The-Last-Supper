package com.goorm.thelastsupper.account.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Account extends BaseEntity {

    private String email;

    private String nickName;

    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_deleted")
    private boolean deleted;

    private LocalDateTime deletedAt;

    private LocalDateTime deletionScheduledAt;

    public void update(String nickName, String phoneNumber) {
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
