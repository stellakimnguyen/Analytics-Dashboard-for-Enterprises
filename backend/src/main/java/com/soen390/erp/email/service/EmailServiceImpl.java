package com.soen390.erp.email.service;

import com.soen390.erp.email.model.EmailToSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    public void sendMail(EmailToSend email){
        System.out.println("sending email");
        emailSender.send(email.getEmailMessage());
        System.out.println("sent email");
    }
}
