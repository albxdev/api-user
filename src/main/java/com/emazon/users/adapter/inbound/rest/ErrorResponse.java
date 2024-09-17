package com.emazon.users.adapter.inbound.rest;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    // Getters y setters
    private int statusCode;
    private String message;
    private String timestamp;

    public ErrorResponse(int statusCode, String message, String timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

}
