package com.grimoire.loyalty.customers;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Optional<Customer> fetchCustomerWithTransactions(String email){
        return repository.fetchCustomerByEmailWithTransactions(email);
    }

    public Customer findOrCreateCustomer(String email){
        return repository
            .fetchCustomerByEmailWithTransactions(email)
            .orElseGet(() -> repository.save(new Customer(email)));
    }
}
