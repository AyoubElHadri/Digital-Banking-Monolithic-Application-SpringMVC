package com.example.digitalbank.services;

import com.example.digitalbank.dtos.*;
import com.example.digitalbank.exceptions.BankAccountNotFoundException;
import com.example.digitalbank.exceptions.CustomerNotFoundException;
import com.example.digitalbank.exceptions.BalanceNotSufficientException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BankAccountService {
    CustomerDto saveCustomer(CustomerDto customerDto);
    CurrentAccountDto saveCurrentAccount(
            double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDto saveSavingAccount(
            double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDto> listCustomers();
    BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description)
            throws BalanceNotSufficientException, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount )
            throws BalanceNotSufficientException, BankAccountNotFoundException;

    List<BankAccountDto> bankAccountList();
    CustomerDto getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDto updateCustomer(CustomerDto customerDto);
    void deleteCustomer(Long customerId);
    List<AccountOperationDto> accountHistory(String accountId);


    AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
