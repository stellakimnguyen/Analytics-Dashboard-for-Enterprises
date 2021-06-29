package com.soen390.erp.inventory.exceptions;

public class NotEnoughMaterialInPlantException extends RuntimeException{
    public NotEnoughMaterialInPlantException(String ex){
        super(ex);
    }
}
