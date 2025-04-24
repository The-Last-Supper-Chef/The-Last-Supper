package com.goorm.thelastsupper.reservation.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_slot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ReservationSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "plan_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private ReservationPlan plan;

    private LocalDate date;

    private LocalTime startTime;

    private int capacityTotal;  // 총 인원

    private int remaining;      // 잔여 인원

    @Enumerated(EnumType.STRING)
    private SlotStatus state;

    // JPA의 Dirty Checking을 통해 트랜잭션 커밋 시 변경 사항이 자동 반영됩니다.
    // 별도의 save 호출 없이도 remaining 값은 DB에 반영됩니다.
    public void decreaseRemaining(int totalVisitors){
        remaining -= totalVisitors;
    }
    public void increaseRemaining(Long totalVisitors){
        remaining += totalVisitors;
    }

    // JPA의 Dirty Checking을 통해 트랜잭션 커밋 시 변경 사항이 자동 반영됩니다.
    // 별도의 save 호출 없이도 SlotStatus 값은 DB에 반영됩니다.
    public void setHold() {
        this.state = SlotStatus.HOLD;
    }
    public void setOpen() {
        this.state = SlotStatus.OPEN;
    }
}
