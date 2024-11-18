package com.grimoire.loyalty.utils.errors;

public abstract class RequestError extends RuntimeException {

    public RequestError(){
        super(null, null, false, false);
    }

    public abstract int statusCode();
    public abstract RequestErrorResponse error();
}
