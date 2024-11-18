package com.grimoire.loyalty.utils.errors;

public final class NotFoundError extends RequestError {
    
    private final RequestErrorResponse error;

    public NotFoundError(){
        super();
        this.error = new RequestErrorResponse("Not Found");
    }

    public NotFoundError(String message){
        super();
        this.error = new RequestErrorResponse(message); 
    }

    @Override
    public int statusCode() {
        return 404;
    }

    @Override
    public RequestErrorResponse error() {
        return error;
    }
}
