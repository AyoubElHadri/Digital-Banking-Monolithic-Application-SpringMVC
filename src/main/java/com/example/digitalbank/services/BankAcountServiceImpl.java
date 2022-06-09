package com.example.digitalbank.services;

import com.example.digitalbank.dtos.*;
import com.example.digitalbank.entities.*;
import com.example.digitalbank.enumerations.OperationType;
import com.example.digitalbank.exceptions.BalanceNotSufficientException;
import com.example.digitalbank.exceptions.BankAccountNotFoundException;
import com.example.digitalbank.exceptions.CustomerNotFoundException;
import com.example.digitalbank.mappers.BankAccountMapperImpl;
import com.example.digitalbank.repositories.AccountOperationsRepository;
import com.example.digitalbank.repositories.BankAccountRepository;
import com.example.digitalbank.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAcountServiceImpl implements BankAccountService{
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationsRepository operationsRepository;
    private BankAccountMapperImpl mapper;


    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto){
        log.info("saving new customer");
        Customer customer = mapper.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }


    @Override
    public CurrentAccountDto saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return mapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingAccountDto saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDTOS = customers
                .stream().map(customer -> mapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return mapper.fromSavingAccount(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return mapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not Sufficent");
        AccountOperations accountOperations = new AccountOperations();
        accountOperations.setType(OperationType.DEBIT);
        accountOperations.setAmount(amount);
        accountOperations.setDescription(description);
        accountOperations.setOperationDate(new Date());
        accountOperations.setBankAccount(bankAccount);
        operationsRepository.save(accountOperations);
        bankAccount.setBalance(bankAccount.getBalance()- amount);
        bankAccountRepository.save(bankAccount);



    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
        AccountOperations accountOperations = new AccountOperations();
        accountOperations.setType(OperationType.CREDIT);
        accountOperations.setAmount(amount);
        accountOperations.setDescription(description);
        accountOperations.setOperationDate(new Date());
        accountOperations.setBankAccount(bankAccount);
        operationsRepository.save(accountOperations);
        bankAccount.setBalance(bankAccount.getBalance()+ amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficientException, BankAccountNotFoundException {
        debit(accountIdSource,amount, "Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+ accountIdSource);
    }

    @Override
    public List<BankAccountDto> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDto> bankAccountDTOS = bankAccounts
                .stream().map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount){
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return mapper.fromSavingAccount(savingAccount);
                    } else {
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return mapper.fromCurrentAccount(currentAccount);
                    }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDto getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
        return mapper.fromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Updating a customer");
        Customer customer= mapper.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);

    }

    @Override
    public List<AccountOperationDto> accountHistory(String accountId) {
        List<AccountOperations> operations = operationsRepository.findByBankAccountId(accountId);
        return operations.stream().map(op->mapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDto getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount ==null) throw new BankAccountNotFoundException("Account not found");
        Page<AccountOperations> accountOperations = operationsRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
        List<AccountOperationDto> accountOperationDtos =accountOperations.getContent().stream().map(op-> mapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDto.setAccountOperationDtos(accountOperationDtos);
        accountHistoryDto.setAccountId(bankAccount.getId());
        accountHistoryDto.setBalance(bankAccount.getBalance());
        accountHistoryDto.setCurrentPage(page);
        accountHistoryDto.setPageSize(size);
        accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDto;
    }
}
