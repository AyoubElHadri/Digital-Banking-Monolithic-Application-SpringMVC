package com.example.digitalbank;

import com.example.digitalbank.dtos.BankAccountDto;
import com.example.digitalbank.dtos.CurrentAccountDto;
import com.example.digitalbank.dtos.CustomerDto;
import com.example.digitalbank.dtos.SavingAccountDto;
import com.example.digitalbank.exceptions.CustomerNotFoundException;
import com.example.digitalbank.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Mike","Hector","Job").forEach(name->{
                CustomerDto customer=new CustomerDto();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer->{
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*90000,9000,customer.getAccountID());
                    bankAccountService.saveSavingAccount(Math.random()*120000,5.5,customer.getAccountID());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDto> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDto bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingAccountDto){
                        accountId=((SavingAccountDto) bankAccount).getId();
                    } else{
                        accountId=((CurrentAccountDto) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
        };


    }
}
