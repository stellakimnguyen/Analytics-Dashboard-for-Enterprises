package com.soen390.erp.manufacturing.exceptions;

public class BikeNotFoundException extends RuntimeException{

    public BikeNotFoundException(int id) {
        super("Could not find bike " + id);
    }
}
