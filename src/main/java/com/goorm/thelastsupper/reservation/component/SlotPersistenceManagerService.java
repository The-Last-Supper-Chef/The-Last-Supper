package com.goorm.thelastsupper.reservation.component;

import com.goorm.thelastsupper.reservation.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.exception.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.exception.ReservationException;
import com.goorm.thelastsupper.reservation.exception.SlotAlreadyExistsException;
import com.goorm.thelastsupper.reservation.repository.ReservationSlotRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 슬롯 중복 검사, 저장, 예외 처리를 담당하는 컴포넌트입니다.
 * - 결합도: ReservationSlotRepository
 * - 응집도: 슬롯 저장 및 중복 검사와 관련된 모든 로직 담당
 */
@Component
@RequiredArgsConstructor
public class SlotPersistenceManagerService {

	private final ReservationSlotRepository slotRepo;

	/**
	 * 슬롯들을 저장하는 메서드.
	 * 중복 검사 후, 저장을 시도하며 예외 처리를 담당합니다.
	 * @param slots 저장할 예약 슬롯 리스트
	 */
	public void persist(List<ReservationSlot> slots) {
		if (slots == null || slots.isEmpty()) return;

		checkForDuplicateSlots(slots);
		saveSlots(slots);
	}

	/**
	 * 예약 슬롯의 중복을 확인하는 메서드.
	 * @param slots 중복 검사할 예약 슬롯 리스트
	 */
	private void checkForDuplicateSlots(List<ReservationSlot> slots) {
		for (var slot : slots) {
			if (exists(slot.getSlotDateTime())) {
				throw new SlotAlreadyExistsException(
					ReservationErrorCode.DUPLICATE_SLOT_EXISTS,
					"슬롯 " + slot.getSlotDateTime() + "은 이미 존재합니다."
				);
			}
		}
	}

	/**
	 * 슬롯이 이미 존재하는지 확인하는 메서드.
	 * @param dt 확인할 슬롯의 날짜와 시간
	 * @return 중복 슬롯이 있으면 true, 없으면 false
	 */
	private boolean exists(LocalDateTime dt) {
		return slotRepo.existsBySlotDateTime(dt);
	}

	/**
	 * 슬롯을 저장하는 메서드.
	 * 중복 검사가 완료된 후, 슬롯을 저장하고 즉시 flush하여 제약 위반을 검출합니다.
	 * @param slots 저장할 예약 슬롯 리스트
	 */
	private void saveSlots(List<ReservationSlot> slots) {
		try {
			slotRepo.saveAll(slots);
			slotRepo.flush();  // 즉시 INSERT → 제약 위반 검출
		} catch (OptimisticLockException ex) {
			handleOptimisticLockException(ex);
		} catch (DataIntegrityViolationException ex) {
			handleDataIntegrityViolationException(ex);
		} catch (DataAccessException ex) {
			throw new ReservationException(ReservationErrorCode.SLOT_SAVE_FAILURE, ex);
		}
	}

	/**
	 * 낙관적 락 예외를 처리하는 메서드.
	 * @param ex 발생한 낙관적 락 예외
	 */
	private void handleOptimisticLockException(OptimisticLockException ex) {
		throw new ReservationException(ReservationErrorCode.DUPLICATE_SLOT_EXISTS, ex);
	}

	/**
	 * 데이터 무결성 위반 예외를 처리하는 메서드.
	 * @param ex 발생한 데이터 무결성 위반 예외
	 */
	private void handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		var root = ex.getMostSpecificCause();
		if (root instanceof org.hibernate.exception.ConstraintViolationException cve
			&& "uk_slot_datetime".equalsIgnoreCase(cve.getConstraintName())) {
			throw new ReservationException(ReservationErrorCode.DUPLICATE_SLOT_EXISTS, ex);
		}
		throw new ReservationException(ReservationErrorCode.SLOT_SAVE_FAILURE, ex);
	}
}
