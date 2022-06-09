package com.example.digitalbank.mappers;

import com.example.digitalbank.dtos.AccountOperationDto;
import com.example.digitalbank.dtos.CurrentAccountDto;
import com.example.digitalbank.dtos.CustomerDto;
import com.example.digitalbank.dtos.SavingAccountDto;
import com.example.digitalbank.entities.AccountOperations;
import com.example.digitalbank.entities.CurrentAccount;
import com.example.digitalbank.entities.Customer;
import com.example.digitalbank.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDto fromCustomer(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        return customerDto;
    }

    public Customer fromCustomerDto(CustomerDto customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }

    public SavingAccountDto fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDto savingAccountDto = new SavingAccountDto();
        BeanUtils.copyProperties(savingAccount, savingAccountDto);
        savingAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingAccountDto.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDto;
    }

    public SavingAccount fromSavingAccountDto(SavingAccountDto savingAccountDto){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDto, savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingAccountDto.getCustomerDto()));
        return savingAccount;
    }

    public CurrentAccountDto fromCurrentAccount(CurrentAccount currentAccount){
        CurrentAccountDto currentAccountDto = new CurrentAccountDto();
        BeanUtils.copyProperties(currentAccount, currentAccountDto);
        currentAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentAccountDto.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDto;
    }

    public CurrentAccount fromCurrentAccountDto(CurrentAccountDto currentAccountDto){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDto, currentAccount);
        currentAccount.setCustomer(fromCustomerDto(currentAccountDto.getCustomerDto()));
        return currentAccount;
    }

    public AccountOperationDto fromAccountOperation(AccountOperations accountOperations){
        AccountOperationDto accountOperationDto = new AccountOperationDto();
        BeanUtils.copyProperties(accountOperations,accountOperationDto);
        return accountOperationDto;
    }
}
