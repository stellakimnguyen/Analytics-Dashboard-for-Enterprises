package com.soen390.erp.configuration.model;

import lombok.Getter;

@Getter
public class BooleanWrapper {
    private boolean result;
    private String message;

    public BooleanWrapper(boolean result, String message){
        this.result = result;
        this.message = message;
    }
}
