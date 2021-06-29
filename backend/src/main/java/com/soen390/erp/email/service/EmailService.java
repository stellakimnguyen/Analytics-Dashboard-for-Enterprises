package com.soen390.erp.email.service;

import com.soen390.erp.email.model.EmailToSend;

public interface EmailService {

    public void sendMail(EmailToSend email);
}
