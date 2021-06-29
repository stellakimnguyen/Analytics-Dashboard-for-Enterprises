package com.soen390.erp.inventory.exceptions;

public class PlantBikeNotFoundException extends RuntimeException{
    public PlantBikeNotFoundException(int id) {
        super("Could not find plant bike " + id);
    }
}
