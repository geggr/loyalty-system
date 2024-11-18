package com.grimoire.loyalty.reedem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grimoire.loyalty.reedem.input.ReedemProductRequest;
import com.grimoire.loyalty.reedem.output.SuccessReedemProductResponse;

@RestController
@RequestMapping("/api/reedem")
public class ReedemProductController {

    private final ReedemProductService service;
    
    public ReedemProductController(ReedemProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> handleReedemProduct(@RequestBody ReedemProductRequest request){
        final var claim = service.execute(request);
        final var response = new SuccessReedemProductResponse("Successfully reedem product!", claim);
        return ResponseEntity.ok(response);
    }
}
