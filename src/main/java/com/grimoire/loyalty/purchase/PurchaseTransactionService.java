package com.grimoire.loyalty.purchase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.grimoire.loyalty.actions.LoyaltyAction;
import com.grimoire.loyalty.customers.Customer;
import com.grimoire.loyalty.customers.CustomerPointTransaction;
import com.grimoire.loyalty.customers.CustomerService;
import com.grimoire.loyalty.purchase.input.CreatePointTransactionRequest;
import com.grimoire.loyalty.purchase.output.SuccessPointTransaction;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class PurchaseTransactionService {
    
    private final CustomerService customerService;
    private final EntityManager manager;

    public PurchaseTransactionService(CustomerService customerService, EntityManager manager) {
        this.customerService = customerService;
        this.manager = manager;
    }
    
    @Transactional
    public SuccessPointTransaction execute(CreatePointTransactionRequest transaction){
        var customer = customerService.findOrCreateCustomer(transaction.customerEmail());

        handleAddTransactionToCustomer(customer, transaction);

        var points = customer.getCurrentTotalPoints();

        return new SuccessPointTransaction(transaction.customerEmail(), points);
    }

    private void handleAddTransactionToCustomer(Customer customer, CreatePointTransactionRequest transaction){
        final var points = LoyaltyAction.PURCHASE.getPointsFor(transaction.amount());
        final var timestamp = LocalDateTime.parse(transaction.date());
        final var pointTransaction = new CustomerPointTransaction(customer, points, "Purchase", timestamp);
        manager.persist(pointTransaction);
        customer.addTransaction(pointTransaction);
    }
}
