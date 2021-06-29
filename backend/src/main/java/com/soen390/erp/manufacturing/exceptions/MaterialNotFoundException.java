package com.soen390.erp.manufacturing.exceptions;

public class MaterialNotFoundException extends RuntimeException{

    public MaterialNotFoundException(int id) {
        super("Could not find material" + id);
    }
}
