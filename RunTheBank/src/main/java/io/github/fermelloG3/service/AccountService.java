package io.github.fermelloG3.service;

import io.github.fermelloG3.domain.entity.Account;
import io.github.fermelloG3.domain.entity.Customer;
import io.github.fermelloG3.domain.repository.AccountRepository;
import io.github.fermelloG3.rest.dto.AccountDTO;
import io.github.fermelloG3.rest.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> finAccountsById(Long accountId){
        return accountRepository.findById(accountId);
    }

    public Account createAccount(CustomerDTO customerDTO){
        Account newAccount = new Account();
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setActive(true);

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setDocument(customerDTO.getDocument());
        customer.setAdress(customerDTO.getAdress());
        customer.setPassword(customerDTO.getPassword());

        newAccount.setCustomer(customer);

        return accountRepository.save(newAccount);
    }

    public Account updateAccount(Long customerId, CustomerDTO customerDTO) {

        Account existingAccount = accountRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Account not found"));


        if (existingAccount.getCustomer() != null) {
            Customer existingCustomer = existingAccount.getCustomer();
            existingCustomer.setName(customerDTO.getName());
            existingCustomer.setPassword(customerDTO.getPassword());
            existingCustomer.setAdress(customerDTO.getAdress());


            return accountRepository.save(existingAccount);
        } else {
            throw new IllegalStateException("Customer is null for the existing account.");
        }
    }

    public void deleteAccount(Long accountId){
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(()-> new NoSuchElementException("Account not found"));
        accountRepository.delete(existingAccount);
    }
}
