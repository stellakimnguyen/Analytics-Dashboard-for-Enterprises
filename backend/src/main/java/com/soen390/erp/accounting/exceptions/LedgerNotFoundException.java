package com.soen390.erp.accounting.exceptions;

public class LedgerNotFoundException extends RuntimeException {

    public LedgerNotFoundException(int id){
        super("Could not find the ledger "+ id);
    }
}
