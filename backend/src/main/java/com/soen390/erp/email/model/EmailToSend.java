package com.soen390.erp.email.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.mail.SimpleMailMessage;

import javax.validation.constraints.Email;


@Data
@Builder
public class EmailToSend {

    //we can create an email account later and set this here. This is fake
    private static final String from = "noreply-soen390@gmail.com";
    @Email
    private String to;
    private String subject;
    @NonNull
    private String body;

    public SimpleMailMessage getEmailMessage(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        return mailMessage;
    }
}
