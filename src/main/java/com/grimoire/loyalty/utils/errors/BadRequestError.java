package com.grimoire.loyalty.utils.errors;

import java.util.List;

public final class BadRequestError extends RequestError {
    private final RequestErrorResponse error;

    public BadRequestError(String message){
        super();
        this.error = new RequestErrorResponse(message);
    }

    public BadRequestError(String message, String code, String detail){
        super();
        this.error = new RequestErrorResponse(message, List.of(new RequestFieldError(code, detail)));
    }

    public BadRequestError(String message, List<RequestFieldError> errors){
        super();
        this.error = new RequestErrorResponse(message, errors);
    }

    @Override
    public int statusCode() {
        return 400;
    }

    public RequestErrorResponse error() {
        return error;
    }
}
