package com.grimoire.loyalty.purchase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grimoire.loyalty.purchase.input.CreatePointTransactionRequest;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseTransactionControler {
    
    private final PurchaseTransactionService service;

    public PurchaseTransactionControler(PurchaseTransactionService service) {
        this.service = service;
    }

    @PostMapping("/add")
    private ResponseEntity<?> handleAddTransactionToCustomer(@RequestBody CreatePointTransactionRequest request){
        return ResponseEntity.ok(service.execute(request));
    }
}
