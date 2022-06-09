package com.example.digitalbank.controllers;

import com.example.digitalbank.dtos.AccountOperationDto;
import com.example.digitalbank.dtos.BankAccountDto;
import com.example.digitalbank.exceptions.BankAccountNotFoundException;
import com.example.digitalbank.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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


}
