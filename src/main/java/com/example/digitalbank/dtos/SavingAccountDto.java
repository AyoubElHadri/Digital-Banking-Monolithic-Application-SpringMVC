package com.example.digitalbank.dtos;

import com.example.digitalbank.enumerations.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingAccountDto extends BankAccountDto{
    private String id;
    private Date createdAt;
    private double balance;
    private AccountStatus status;
    private CustomerDto customerDto;
    private double interestRate;

}
