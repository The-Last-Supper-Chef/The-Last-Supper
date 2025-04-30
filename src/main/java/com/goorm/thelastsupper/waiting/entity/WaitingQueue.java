package com.goorm.thelastsupper.waiting.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.account.entity.Account;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "waiting_queue")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WaitingQueue extends BaseEntity {

    // 웨이팅 상태: 대기중, 취소, 완료, 딜레이
    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    // 신청 인원
    private int headCount;

    // 대기 번호
    private Long number;

    // 계정 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Account account;
}
