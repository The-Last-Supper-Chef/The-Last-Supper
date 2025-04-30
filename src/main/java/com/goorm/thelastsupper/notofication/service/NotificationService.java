package com.goorm.thelastsupper.notofication.service;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.notofication.entity.NotificationEventType;
import com.goorm.thelastsupper.notofication.listener.StompEventListener;
import com.goorm.thelastsupper.reservation.entity.ReservationHistory;
import com.goorm.thelastsupper.reservation.entity.ReservationPlan;
import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.reservation.repository.ReservationPlanRepository;
import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.goorm.thelastsupper.notofication.dto.ReservationRegisterNotificationDto.createReservationNotificationDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final StompEventListener stompEventListener;
    private final ReservationPlanRepository reservationPlanRepository;
    private final RestaurantRepository restaurantRepository;
    private final String DESTINATION_SUBSCRIBE_URL = "/queue/notifications";

    public void sendRegisterReservationNotification(Account account, ReservationSlot reservationSlot, ReservationHistory reservationHistory) {

        String planId = reservationSlot.getPlan().getId();
        ReservationPlan reservationPlan = reservationPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.warn("예약 플랜을 찾을 수 없습니다. planId={}", planId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_PLAN_NOT_FOUND);
                });

        String restaurantId = reservationPlan.getRestaurant().getId();
        Restaurant restaurant = restaurantRepository.findByAccountId(restaurantId)
                .orElseThrow(() -> {
                    log.warn("레스토랑을 찾을 수 없습니다. restaurantId={}", restaurantId);
                    return new ReservationException(ReservationErrorCode.RESERVATION_PLAN_NOT_FOUND);
                });

        Set<String> sessions = stompEventListener.getSessions();
        //Todo : JWT 도입시 JWT : Session 맵을 통해 구분
        //Todo : title, message, category ENUM으로 추출
        //Todo : Service단에서 알림을 요청할때 어떤 Service에 대한 알림인지에 관한 정보를 넘김
        //Todo : 그에 맞는 Enum을 찾아서 반환
        int count = 0;
        for (String sessionId : sessions) {
            if (count == 0) {
                // Customer notification
                messagingTemplate.convertAndSendToUser(
                        sessionId,
                        DESTINATION_SUBSCRIBE_URL,
                        createReservationNotificationDto(account,reservationSlot,reservationHistory,restaurant, NotificationEventType.RESERVATION_REGISTER_CUSTOMER),
                        createHeaders(sessionId)
                );
                count++;
            } else {
                // Owner notification
                messagingTemplate.convertAndSendToUser(
                        sessionId,
                        DESTINATION_SUBSCRIBE_URL,
                        createReservationNotificationDto(account,reservationSlot,reservationHistory,restaurant,NotificationEventType.RESERVATION_REGISTER_OWNER),
                        createHeaders(sessionId)
                );
            }
        }
    }

    private MessageHeaders createHeaders(@Nullable String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        if (sessionId != null) {
            headerAccessor.setSessionId(sessionId);
        }
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}


