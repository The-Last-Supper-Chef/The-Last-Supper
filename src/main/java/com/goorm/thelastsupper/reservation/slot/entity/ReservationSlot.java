package com.goorm.thelastsupper.reservation.slot.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import com.goorm.thelastsupper.reservation.plan.entity.SlotStatus;
import com.goorm.thelastsupper.reservation.plan.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.slot.dto.ReservationSlotDTO;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "reservation_slot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSlot extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ReservationPlan plan;

    private LocalDate date;
    private LocalTime startTime;

    /** 날짜 + 시각 합성 (UNIQUE) */
    @Column(name = "slot_date_time", nullable = false, unique = true)
    private LocalDateTime slotDateTime;

    private int       capacityTotal;
    private int       remaining;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    /* -------- 생성 팩터리 -------- */
    public static ReservationSlot of(ReservationPlan plan,
        LocalDate date,
        LocalTime startTime,
        SlotStatus status,
        int capacity) {

        ReservationSlot s = new ReservationSlot();
        s.plan          = plan;
        s.date          = date;
        s.startTime     = startTime;
        s.slotDateTime  = LocalDateTime.of(date, startTime);
        s.capacityTotal = capacity;
        s.remaining     = capacity;
        s.status        = status;
        return s;
    }

    public void decreaseRemaining(int totalVisitors){
        remaining -= totalVisitors;
    }
    public void increaseRemaining(int totalVisitors){
        remaining += totalVisitors;
    }

    public void setHold() {
        this.status = SlotStatus.HOLD;
    }
    public void setOpen() {
        this.status = SlotStatus.OPEN;
    }
    public void setBlock() {
        this.status = SlotStatus.BLOCK;
    }

    public ReservationSlotDTO toDTO() {
        return ReservationSlotDTO.from(this);
    }


}
