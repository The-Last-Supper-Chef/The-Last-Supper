package com.goorm.thelastsupper.notofication.dto;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.notofication.entity.NotificationCategory;
import com.goorm.thelastsupper.notofication.entity.NotificationEventType;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRegisterNotificationDto(
        String accountId,
        String ownerId,
        LocalDate date,
        LocalTime time,
        int reservedPeople,
        String customerName,
        String restaurantName,
        String restaurantLocation,
        String request,
        String title,
        String message,
        NotificationCategory category
) {
    public static  ReservationRegisterNotificationDto createReservationNotificationDto(
            Account account, ReservationSlot slot, ReservationHistory history, Restaurant restaurant, NotificationEventType notificationEventType
    ) {
        return new ReservationRegisterNotificationDto(
                account.getId(),
                restaurant.getAccount().getId(),
                slot.getDate(),
                slot.getStartTime(),
                history.getReservedPeople(),
                account.getNickName(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantLocation(),
                history.getRequest() != null ? history.getRequest() : "없음",
                notificationEventType.getTitle(),
                notificationEventType.getMessage(),
                notificationEventType.getCategory()
        );
    }
}