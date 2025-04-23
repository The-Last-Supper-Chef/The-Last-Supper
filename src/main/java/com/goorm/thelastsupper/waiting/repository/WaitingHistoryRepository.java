package com.goorm.thelastsupper.waiting.repository;

import com.goorm.thelastsupper.waiting.entity.WaitingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingHistoryRepository extends JpaRepository <WaitingHistory, String> {
}
