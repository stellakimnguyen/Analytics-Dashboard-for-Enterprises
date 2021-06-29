package com.soen390.erp.accounting.exceptions;

public class AccountNotFoundException extends  RuntimeException {

    public AccountNotFoundException(int id) {
        super("Could not find the account " + id);
    }
}