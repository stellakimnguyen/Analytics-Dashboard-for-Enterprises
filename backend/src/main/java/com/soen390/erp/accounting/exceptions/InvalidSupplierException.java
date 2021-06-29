package com.soen390.erp.accounting.exceptions;

public class InvalidSupplierException extends RuntimeException{
    public InvalidSupplierException () {
        super("Invalid Supplier ");
    }
}
