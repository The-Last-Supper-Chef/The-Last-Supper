package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingRequest;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import com.goorm.thelastsupper.waiting.exception.WaitingErrorCode;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import com.goorm.thelastsupper.waiting.repository.WaitingSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WaitingSettingService {

    private final RestaurantRepository restaurantRepository;
    private final WaitingSettingRepository waitingSettingRepository;

    @Transactional
    public WaitingSettingResponse openWaiting(WaitingSettingRequest request) {

        // 1. 매장 존재 여부 확인
        Restaurant restaurant  = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new WaitingException(WaitingErrorCode.RESTAURANT_NOT_FOUND));

        // 2. 대기열 웨이팅이 이미 오픈인지 확인
        boolean isAlreadyOpen = waitingSettingRepository.existsByRestaurant_IdAndWaitingSetCategory(
                request.restaurantId(), WaitingSetCategory.OPEN
        );

        if (isAlreadyOpen) {
            throw new WaitingException(WaitingErrorCode.WAITING_ALREADY_OPEN);
        }

        // 3. 요청 DTO -> Entity 변환 (빌더 생성 책임은 DTO로 이동)
        WaitingSetting setting = request.toEntity(restaurant); //DTO SettingRequest

        //4. 저장
        waitingSettingRepository.save(setting);

        // 5. Entity -> 응답 DTO 변환
        return WaitingSettingResponse.from(setting); //DTO SettingResponse

    }
}
