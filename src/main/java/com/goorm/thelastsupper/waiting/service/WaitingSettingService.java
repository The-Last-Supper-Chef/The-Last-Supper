package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.restaurant.entity.Restaurant;
import com.goorm.thelastsupper.restaurant.repository.RestaurantRepository;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingFindRequest;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingRequest;
import com.goorm.thelastsupper.waiting.dto.WaitingSettingResponse;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import com.goorm.thelastsupper.waiting.repository.WaitingSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaitingSettingService {

    private final RestaurantRepository restaurantRepository;
    private final WaitingSettingRepository waitingSettingRepository;
    private final WaitingQueueRepository waitingQueueRepository;

    //Message : 기존 코드 설명 후 삭제 예정
//        // 1. 매장 존재 여부 확인
//        Restaurant restaurant  = restaurantRepository.findById(request.restaurantId())
//                .orElseThrow(WaitingException.RestaurantNotFoundException::new);
//        // 2. 매장의 최신 대기 상태 값 확인
//        Optional<WaitingSetting> latestWaitingSetting = waitingSettingRepository.findFirstByRestaurant_IdOrderByIdDesc(request.restaurantId());

    @Transactional
    public WaitingSettingFindRequest findRestaurantAndSetting(String id) {
        //1. 매장 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(WaitingException.RestaurantNotFoundException::new);

        //2. 매장의 최신 대기 상태 값 확인
        Optional<WaitingSetting> latestWaitingSetting = waitingSettingRepository
                .findFirstByRestaurant_IdOrderByIdDesc(id);

        // 3. 반환할 DTO 생성하여 리턴
        return new WaitingSettingFindRequest(restaurant, latestWaitingSetting);
    }


    @Transactional
    public WaitingSettingResponse openWaiting(WaitingSettingRequest request) {
        // 매장 존재 여부 확인 + 매장의 최신 대기 상태 값 확인 리팩토링
        WaitingSettingFindRequest findRequest = findRestaurantAndSetting(request.restaurantId());

        //로그로 값 확인
        log.info("매장 정보: {}", findRequest.restaurant()); //매장 정보 로그

        // 최신 대기 설정 상태가 존재하면 출력, 없으면 "없음"을 출력
        if (findRequest.latestWaitingSetting().isPresent()) {
            log.info("최신 대기 설정 상태: {}", findRequest.latestWaitingSetting().get().getWaitingSetCategory());
        } else {
            log.info("최신 대기 설정 상태: 없음");
        }

        //2. 최신 대기 상태가 OPEN일 경우 예외 처리
        if(findRequest.latestWaitingSetting().isPresent()) {
            WaitingSetting setting = findRequest.latestWaitingSetting().get();
            if (setting.getWaitingSetCategory() == WaitingSetCategory.OPEN) {
                throw new WaitingException.WaitingAlreadyOpenException();
            }
        }

        // 3. 대기열 상태를 OPEN으로 설정하여 저장
        WaitingSetting setting = request.toEntity(findRequest.restaurant());
        waitingSettingRepository.save(setting);

        // 로그로 저장된 대기 상태 확인
        log.info("새로운 대기 상태가 OPEN으로 설정됨: {}", setting.getWaitingSetCategory());

        // 4. Entity -> 응답 DTO 변환
        return WaitingSettingResponse.from(setting);
    }

    /*Message: 중단(pauseWaiting)
        웨이팅 대기열 설정이 "오픈" 일때만 가능하도록
        웨이팅 대기열 설정이 "영업종료" => ("대기열이 열려있지 않습니다.")
        웨이팅 대기열 설정이 "중단" => ("대기열이 이미 중단되어 있습니다.")
     */
    @Transactional
    public WaitingSettingResponse pauseWaiting(WaitingSettingRequest request) {
        // 매장 존재 여부 확인 + 매장의 최신 대기 상태 값 확인 리팩토링
        WaitingSettingFindRequest findRequest = findRestaurantAndSetting(request.restaurantId());

        // 3. 최신 상태가 OPEN이 아니면 예외 처리
        if (findRequest.latestWaitingSetting().isPresent()) {
            WaitingSetting latest = findRequest.latestWaitingSetting().get(); // Optional 안의 값 꺼내기

            // 만약 상태가 PAUSE라면 이미 중단된 상태이므로 예외 던짐
            if (latest.getWaitingSetCategory() == WaitingSetCategory.PAUSE) {
                throw new WaitingException.WaitingAlreadyPausedException();
            }

            // 만약 상태가 OPEN이 아니면 대기열이 열리지 않은 상태로 예외 던짐
            if (latest.getWaitingSetCategory() != WaitingSetCategory.OPEN) {
                throw new WaitingException.WaitingNotOpendException();
            }

        } else {
            // 최신 대기 상태 값이 없다면 대기열이 열리지 않은 상태로 예외 던짐
            throw new WaitingException.WaitingNotOpendException();
        }

        // 4. 중단 상태로 저장
        WaitingSetting setting = request.toEntity(findRequest.restaurant());
        waitingSettingRepository.save(setting);

        // 5. 응답 DTO 반환
        return WaitingSettingResponse.from(setting);
    }

    /*Message: 종료(closeWaiting)
       1. 웨이팅 설정이 "오픈","중단" 일 경우에만 영업종료 가능하도록 하고
       웨이팅 대기열(WaitingQueue) 내에 "웨이팅 상태" 를 전면 취소 처리
       2. 웨이팅 설정이 이미 "영업종료" 일 경우 => 예외처리 메시지 발생
     */
    @Transactional
    public WaitingSettingResponse closeWaiting(WaitingSettingRequest request) {
        WaitingSettingFindRequest findRequest = findRestaurantAndSetting(request.restaurantId());

        if (findRequest.latestWaitingSetting().isPresent()) {
            WaitingSetting latest = findRequest.latestWaitingSetting().get();

            if (latest.getWaitingSetCategory() == WaitingSetCategory.CLOSE) {
                throw new WaitingException.WaitingAlreadyClosedException(); // -> 예외처리 메시지 발생 (대기열이 이미 종료되었습니다.)
            }
        } else {
            //최신 대기 상태 값이 없다면 대기열이 열리치 않은 상태로 예외 던짐
            throw new WaitingException.WaitingNotOpendException();
        }


        // 3. 대기 상태인 항목 전체 조회 -> 상태를 CANCEL로 변경
        List<WaitingQueue> waitingQueueList = waitingQueueRepository
                .findAllByWaitingStatus(WaitingStatus.WAITING);

        for (WaitingQueue q : waitingQueueList) {
            q.setWaitingStatus(WaitingStatus.CANCEL);
        }

        // 4. 대기열 설정 종료 상태로 저장
        WaitingSetting setting = request.toEntity(findRequest.restaurant());
        waitingSettingRepository.save(setting);

        return WaitingSettingResponse.from(setting);
    }
}
