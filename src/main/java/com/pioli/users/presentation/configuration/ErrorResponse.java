package com.pioli.users.presentation.configuration;

public class ErrorResponse {
    private Integer code;
    private String name;
    private String message;

    public ErrorResponse(Integer code,  String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}