package com.wipro.dai.vmstats.exception;

public class ApiResponseException extends RuntimeException{
    public ApiResponseException(String message) {
        super(message);
    }
}
