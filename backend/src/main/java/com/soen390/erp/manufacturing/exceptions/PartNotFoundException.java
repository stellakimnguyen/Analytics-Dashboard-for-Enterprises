package com.soen390.erp.manufacturing.exceptions;

public class PartNotFoundException extends RuntimeException{

    public PartNotFoundException(int id){
        super("Could not find the part "+ id);
    }
}
