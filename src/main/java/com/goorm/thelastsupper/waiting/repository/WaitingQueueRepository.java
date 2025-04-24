package com.goorm.thelastsupper.waiting.repository;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingQueueRepository extends JpaRepository<WaitingQueue,String> {
    boolean existsByAccountAndWaitingStatus(Account account, WaitingStatus waitingStatus);

    @Query("SELECT MAX(wq.number) FROM WaitingQueue wq")
    Long findMaxNumber();

    Optional<WaitingQueue> findByAccountAndWaitingStatus(Account account, WaitingStatus waitingStatus);
}