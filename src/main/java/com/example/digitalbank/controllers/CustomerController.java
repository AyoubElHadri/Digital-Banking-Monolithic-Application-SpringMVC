package com.example.digitalbank.controllers;

import com.example.digitalbank.dtos.CustomerDto;
import com.example.digitalbank.exceptions.CustomerNotFoundException;
import com.example.digitalbank.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@Slf4j
public class CustomerController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers/{id}")
    public CustomerDto getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }


    @PostMapping("/customers")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto) {
        return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDto updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDto customerDto){
        customerDto.setAccountID(customerId);
        return bankAccountService.updateCustomer(customerDto);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId){
        bankAccountService.deleteCustomer(customerId);
    }

}
