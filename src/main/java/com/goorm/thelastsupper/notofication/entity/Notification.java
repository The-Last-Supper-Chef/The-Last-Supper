package com.goorm.thelastsupper.notofication.entity;

import com.goorm.thelastsupper.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    private String accountId;

    private String ownerId;

    private String date;

    private String time;

    private int reservedPeople;

    private String customerName;

    private String restaurantName;

    private String restaurantLocation;

    private String request;

    private String title;

    private String message;

    private NotificationCategory category;
}

