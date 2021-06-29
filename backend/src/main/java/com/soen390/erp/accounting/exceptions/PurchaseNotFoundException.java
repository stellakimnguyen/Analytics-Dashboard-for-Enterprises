package com.soen390.erp.accounting.exceptions;

public class PurchaseNotFoundException extends RuntimeException{
    public PurchaseNotFoundException(String message){
        super(message);
    }
}
