package com.example.digitalbank.controllers;

import com.example.digitalbank.dtos.AccountHistoryDto;
import com.example.digitalbank.dtos.AccountOperationDto;
import com.example.digitalbank.dtos.BankAccountDto;
import com.example.digitalbank.exceptions.BankAccountNotFoundException;
import com.example.digitalbank.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDto getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDto> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDto> getHistory(@PathVariable(name = "id") String customerId){
        return bankAccountService.accountHistory(customerId);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDto getAcountHistory(
            @PathVariable(name = "id") String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }



}
