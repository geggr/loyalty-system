package com.grimoire.loyalty.redeem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grimoire.loyalty.redeem.input.RedeemProductRequest;
import com.grimoire.loyalty.redeem.output.SuccessRedeemProductResponse;

@RestController
@RequestMapping("/api/reedem")
public class RedeemProductController {

    private final RedeemProductService service;
    
    public RedeemProductController(RedeemProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> handleRedeemProduct(@RequestBody RedeemProductRequest request){
        final var claim = service.execute(request);
        final var response = new SuccessRedeemProductResponse("Successfully reedem product!", claim);
        return ResponseEntity.ok(response);
    }
}
