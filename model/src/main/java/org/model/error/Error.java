package org.model.error;

public class Error {

    private Integer code;

    private String message;

    private String details;

    public Error(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Error(Integer code, String message, String details){
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
