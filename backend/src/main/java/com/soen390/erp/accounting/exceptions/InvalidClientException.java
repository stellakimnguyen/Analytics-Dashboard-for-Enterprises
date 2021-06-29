package com.soen390.erp.accounting.exceptions;

public class InvalidClientException extends RuntimeException{
    public InvalidClientException () {
        super("Invalid Client ");
    }
}
