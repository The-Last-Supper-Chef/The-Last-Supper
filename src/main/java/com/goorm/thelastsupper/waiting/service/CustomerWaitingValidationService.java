package com.goorm.thelastsupper.waiting.service;

import com.goorm.thelastsupper.account.entity.Account;
import com.goorm.thelastsupper.account.repository.AccountRepository;
import com.goorm.thelastsupper.waiting.entity.WaitingQueue;
import com.goorm.thelastsupper.waiting.entity.WaitingSetCategory;
import com.goorm.thelastsupper.waiting.entity.WaitingSetting;
import com.goorm.thelastsupper.waiting.entity.WaitingStatus;
import com.goorm.thelastsupper.waiting.exception.WaitingException;
import com.goorm.thelastsupper.waiting.repository.WaitingQueueRepository;
import com.goorm.thelastsupper.waiting.repository.WaitingSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerWaitingValidationService {
    private final WaitingSettingRepository waitingSettingRepository;
    private final WaitingQueueRepository waitingQueueRepository;
    private final AccountRepository accountRepository;

    public Account validateAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(WaitingException.AccountNotFoundException::new);
    }

    public void validateWaitingSetCategory() {
        WaitingSetting waitingSetting = waitingSettingRepository.findFirstByOrderByIdDesc()
                .orElseThrow(WaitingException.WaitingSettingNotFoundException::new);

        if(waitingSetting.getWaitingSetCategory() != WaitingSetCategory.OPEN){
            throw new WaitingException.WaitingNotOpenException();
        }
    }

    public void validateAlreadyWaiting(Account account) {
        if(waitingQueueRepository.existsByAccountAndWaitingStatus(account, WaitingStatus.WAITING)){
            throw new WaitingException.AlreadyWaitingException();
        }
    }

    public Long findNextNumber() {
        Long maxNumber = waitingQueueRepository.findMaxNumber();
        return (maxNumber == null) ? 1L : maxNumber + 1;
    }

    public WaitingQueue waitingQueueSave(WaitingQueue waitingQueue) {
        return waitingQueueRepository.save(waitingQueue);
    }

    private boolean isLastWaiting(WaitingQueue waitingQueue) {
        Long myNumber = waitingQueue.getNumber();
        Long maxNumber = waitingQueueRepository.findMaxNumber();

        return myNumber != null && myNumber.equals(maxNumber);
    }

    public WaitingQueue waitingQueueCancel(Account account) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByAccountAndWaitingStatus(account, WaitingStatus.WAITING)
                .orElseThrow(WaitingException.WaitingNotFoundException::new);

        waitingQueue.setWaitingStatus(WaitingStatus.CANCEL);
        waitingQueueRepository.save(waitingQueue);

        return waitingQueue;
    }

    public int waitingQueueDelay(Account account) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByAccountAndWaitingStatus(account, WaitingStatus.WAITING)
                .orElseThrow(WaitingException.WaitingNotFoundException::new);

        if (isLastWaiting(waitingQueue)) {
            throw new WaitingException.AlreadyLastWaitingException();
        }

        waitingQueue.setWaitingStatus(WaitingStatus.DELAY);
        waitingQueueRepository.save(waitingQueue);

        return waitingQueue.getHeadCount();
    }

}
