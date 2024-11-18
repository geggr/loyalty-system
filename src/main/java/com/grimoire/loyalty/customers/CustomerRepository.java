package com.grimoire.loyalty.customers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    @Query("FROM Customer c JOIN FETCH c.transactions WHERE c.email = :email")
    Optional<Customer> fetchCustomerByEmailWithTransactions(String email);

    @Query("FROM Customer c JOIN FETCH c.transactions t WHERE c.email = :email GROUP BY t.customer HAVING SUM(t.points) > :amount")
    Optional<Customer> fetchCustomerWithEnoughPoints(@Param("email") String email, @Param("amount") Integer amount);
}
