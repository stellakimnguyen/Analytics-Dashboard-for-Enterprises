package com.soen390.erp.configuration.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String message;
    String user;
    String category;
    Date timestamp;

    public Log(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            this.user = authentication.getName();
        this.timestamp = new Date();
    }

    public Log(String message, String category){
        this();
        this.message = message;
        this.category = category;
    }
}
