package com.backend.foodproject.exception;

public class CustomExceptionHandling extends RuntimeException {
    Integer status;
    public CustomExceptionHandling(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
