package com.goorm.thelastsupper.account.repository;

import com.goorm.thelastsupper.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
