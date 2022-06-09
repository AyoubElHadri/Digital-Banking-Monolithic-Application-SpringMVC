package com.example.digitalbank.repositories;

import com.example.digitalbank.entities.AccountOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountOperationsRepository extends JpaRepository<AccountOperations, Long> {
    List<AccountOperations>  findByBankAccountId(String accountId);
    Page<AccountOperations> findByBankAccountId(String accountId, Pageable pageable);
}
