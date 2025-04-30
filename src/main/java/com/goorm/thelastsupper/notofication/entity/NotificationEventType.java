package com.goorm.thelastsupper.notofication.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationEventType {
    RESERVATION_REGISTER_CUSTOMER("예약 완료", "예약해 주셔서 감사합니다.\n방문을 진심으로 환영합니다!", NotificationCategory.RESERVATION),
    RESERVATION_REGISTER_OWNER("예약 등록","예약이 1건 등록되었습니다.",NotificationCategory.RESERVATION),
    RESERVATION_CANCEL("예약 취소", "예약이 정상적으로 취소되었습니다.", NotificationCategory.RESERVATION),
    WAITING_REGISTER("웨이팅 등록 완료", "웨이팅 등록이 완료되었습니다.", NotificationCategory.WAITING),
    WAITING_CANCEL("웨이팅 취소 완료", "웨이팅이 정상적으로 취소되었습니다.", NotificationCategory.WAITING);

    private final String title;
    private final String message;
    private final NotificationCategory category;

    public static NotificationEventType from(EventType eventType, boolean isOwner) {
        return switch (eventType) {
            case RESERVATION_REGISTER -> isOwner ? RESERVATION_REGISTER_OWNER : RESERVATION_REGISTER_CUSTOMER;
            case RESERVATION_CANCEL -> RESERVATION_CANCEL;
            case WAITING_REGISTER -> WAITING_REGISTER;
            case WAITING_CANCEL -> WAITING_CANCEL;
        };
    }

    // constructor + getter
}