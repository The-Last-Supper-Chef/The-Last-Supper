package com.goorm.thelastsupper.reservation.slot.component;

import com.goorm.thelastsupper.reservation.slot.entity.ReservationSlot;
import com.goorm.thelastsupper.reservation.common.error.ReservationErrorCode;
import com.goorm.thelastsupper.reservation.common.exception.ReservationException;
import com.goorm.thelastsupper.reservation.slot.repository.JpaSlotReadRepository;
import com.goorm.thelastsupper.reservation.slot.repository.JpaSlotWriteRepository;
import com.goorm.thelastsupper.reservation.slot.repository.SlotWriteRepository;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 슬롯 중복 검사, 저장, 예외 처리를 담당하는 컴포넌트입니다.
 * - 결합도: ReservationSlotRepository
 * - 응집도: 슬롯 저장 및 중복 검사와 관련된 모든 로직 담당
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SlotWriteService {
	private final JpaSlotWriteRepository jpaSlotWriteRepository;

	/**
	 * 시간 슬롯들을 저장하는 메서드.
	 * 중복 검사 후, 저장을 시도하며 예외 처리를 담당합니다.
	 * @param slots 저장할 예약 슬롯 리스트
	 */
	public void persist(List<ReservationSlot> slots) {
		if (slots == null || slots.isEmpty()) return;
		saveSlots(slots);
	}

	// ============================== private ============================== //
	/**
	 * 시간 슬롯을 저장하는 메서드.
	 * 중복 검사가 완료된 후, 시간 슬롯을 저장하고 즉시 flush하여 제약 위반을 검출합니다.
	 * @param slots 저장할 예약 슬롯 리스트
	 */
	private void saveSlots(List<ReservationSlot> slots) {
		try {
			jpaSlotWriteRepository.saveAll(slots);
			jpaSlotWriteRepository.flush();  // 즉시 INSERT → 제약 위반 검출
		} catch (OptimisticLockException ex) {
			handleOptimisticLockException(ex);
		} catch (DataIntegrityViolationException ex) {
			handleDataIntegrityViolationException(ex);
		} catch (DataAccessException ex) {
			log.error("ReservationSlotWriteService.saveSlots() - DataAccessException: {}", ex.getMessage());
			throw new ReservationException(ReservationErrorCode.SLOT_SAVE_FAILURE, ex);
		}
	}

	/**
	 * 낙관적 락 예외를 처리하는 메서드.
	 * @param ex 발생한 낙관적 락 예외
	 */
	private void handleOptimisticLockException(OptimisticLockException ex) {
		log.error("ReservationSlotWriteService.saveSlots() - OptimisticLockException: {}", ex.getMessage());
		throw new ReservationException(ReservationErrorCode.DUPLICATE_SLOT_EXISTS, ex);
	}

	/**
	 * 데이터 무결성 위반 예외를 처리하는 메서드.
	 * @param ex 발생한 데이터 무결성 위반 예외
	 */
	private void handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		throw new ReservationException(
			ReservationErrorCode.DUPLICATE_SLOT_EXISTS, ex);
	}

	public void save(ReservationSlot slot) {
		try {
			jpaSlotWriteRepository.save(slot);
			jpaSlotWriteRepository.flush();  // 즉시 INSERT → 제약 위반 검출
		} catch (OptimisticLockException ex) {
			handleOptimisticLockException(ex);
		} catch (DataIntegrityViolationException ex) {
			handleDataIntegrityViolationException(ex);
		} catch (DataAccessException ex) {
			log.error("ReservationSlotWriteService.save() - DataAccessException: {}", ex.getMessage());
			throw new ReservationException(ReservationErrorCode.SLOT_SAVE_FAILURE, ex);
		}
	}
}
