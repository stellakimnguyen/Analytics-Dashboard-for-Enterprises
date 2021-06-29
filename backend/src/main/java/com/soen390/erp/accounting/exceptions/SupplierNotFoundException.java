package com.soen390.erp.accounting.exceptions;

public class SupplierNotFoundException extends RuntimeException{

    public SupplierNotFoundException(int id) {
        super("Could not find supplier " + id);
    }
}