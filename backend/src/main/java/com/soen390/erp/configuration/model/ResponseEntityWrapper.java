package com.soen390.erp.configuration.model;

import lombok.Data;
import org.springframework.http.ResponseEntity;
@Data
public class ResponseEntityWrapper {

    private ResponseEntity responseEntity;
    private String message;

    public ResponseEntityWrapper(ResponseEntity<?> responseEntity, String message){
        this.responseEntity = responseEntity;
        this.message = message;
    }
}
