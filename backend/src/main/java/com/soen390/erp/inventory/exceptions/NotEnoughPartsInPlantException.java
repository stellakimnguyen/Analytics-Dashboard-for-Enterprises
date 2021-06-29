package com.soen390.erp.inventory.exceptions;

public class NotEnoughPartsInPlantException extends RuntimeException{
    public NotEnoughPartsInPlantException(String ex){
        super(ex);
    }
}
