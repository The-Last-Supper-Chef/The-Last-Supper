package com.goorm.thelastsupper.reservation.slot.entity;

public enum SlotStatus {
    OPEN,
    HOLD,
    BLOCK;

    public boolean isNotOpen() {
        return this != OPEN;
    }

    public boolean isBlocked() {
        return this == BLOCK;
    }

    public boolean isHeld() {
        return this == HOLD;
    }
}
