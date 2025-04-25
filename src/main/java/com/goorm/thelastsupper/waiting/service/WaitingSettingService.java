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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitingSettingService {

    private final RestaurantRepository restaurantRepository;
    private final WaitingSettingRepository waitingSettingRepository;

    @Transactional
    public WaitingSettingResponse openWaiting(WaitingSettingRequest request) {

        // 1. 매장 존재 여부 확인
        Restaurant restaurant  = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(WaitingException.RestaurantNotFoundException::new);

        // 2. 매장의 최신 대기 상태 값 확인
        Optional<WaitingSetting> latestWaitingSetting = waitingSettingRepository.findFirstByRestaurant_IdOrderByIdDesc(request.restaurantId());

        // 3. 최신 대기 상태가 OPEN일 경우 예외 처리
        if (latestWaitingSetting.isPresent()) {
            WaitingSetting setting = latestWaitingSetting.get(); // Optional에서 값 꺼내기

            if (setting.getWaitingSetCategory() == WaitingSetCategory.OPEN) {
                throw new WaitingException.WaitingAlreadyOpenException();
            }
        }

        // 4. 대기열 상태를 OPEN으로 설정하여 저장
        WaitingSetting setting = request.toEntity(restaurant); //DTO SettingRequest
        waitingSettingRepository.save(setting);

        // 5. Entity -> 응답 DTO 변환
        return WaitingSettingResponse.from(setting); //DTO SettingResponse

    }

    // 마감(closeWaiting) -> 중단 , 차단(blockWaiting) -> 영업종료
    @Transactional
    public WaitingSettingResponse closeWaiting(WaitingSettingRequest request) {

        // 1. 매장 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(WaitingException.RestaurantNotFoundException::new);

        // 2. 매장의 최신 대기 상태 값 확인
        Optional<WaitingSetting> latestWaitingSetting = waitingSettingRepository.findFirstByRestaurant_IdOrderByIdDesc(request.restaurantId());

        // 3. 최신 상태가 OPEN이 아니면 예외 처리
        if (latestWaitingSetting.isPresent()) {
            WaitingSetting latest = latestWaitingSetting.get(); // Optional 안의 값 꺼내기

            // 만약 상태가 CLOSE라면 이미 마감된 상태이므로 예외 던짐
            if (latest.getWaitingSetCategory() == WaitingSetCategory.CLOSE) {
                throw new WaitingException.WaitingAlreadyClosedException();
            }

            // 만약 상태가 OPEN이 아니면 대기열이 열리지 않은 상태로 예외 던짐
            if (latest.getWaitingSetCategory() != WaitingSetCategory.OPEN) {
                throw new WaitingException.WaitingNotOpendException();
            }

        } else {
            // 최신 대기 상태 값이 없다면 대기열이 열리지 않은 상태로 예외 던짐
            throw new WaitingException.WaitingNotOpendException();
        }

        // 4. 마감 상태로 저장
        WaitingSetting setting = request.toEntity(restaurant);
        waitingSettingRepository.save(setting);

        // 5. 응답 DTO 반환
        return WaitingSettingResponse.from(setting);
    }
}
