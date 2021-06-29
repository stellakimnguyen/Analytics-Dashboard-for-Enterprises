package com.soen390.erp.inventory.exceptions;

public class PlantNotFoundException extends RuntimeException{
    public PlantNotFoundException(int id){
        super("Could not find Plant" + id);
    }
}
