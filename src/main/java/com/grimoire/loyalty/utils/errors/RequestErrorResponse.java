package com.grimoire.loyalty.utils.errors;

import java.util.List;

public record RequestErrorResponse(String message, List<RequestFieldError> errors) {
    public RequestErrorResponse(String message){
        this(message, List.of());
    }
}
