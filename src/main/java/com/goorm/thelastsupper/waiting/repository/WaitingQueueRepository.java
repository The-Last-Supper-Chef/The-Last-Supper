package com.goorm.thelastsupper.waiting.repository;

import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue, String> {
}
