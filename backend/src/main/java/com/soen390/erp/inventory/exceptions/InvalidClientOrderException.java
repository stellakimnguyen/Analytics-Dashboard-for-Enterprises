package com.soen390.erp.inventory.exceptions;

public class InvalidClientOrderException extends RuntimeException {
    public InvalidClientOrderException(){
        super("Invalid Client Order");
    }
}
