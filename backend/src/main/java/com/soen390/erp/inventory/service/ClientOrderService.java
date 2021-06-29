package com.soen390.erp.inventory.service;

import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import com.soen390.erp.inventory.exceptions.InvalidClientOrderException;
import com.soen390.erp.inventory.model.ClientOrder;
import com.soen390.erp.inventory.repository.ClientOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final EmailService emailService;
    public ClientOrderService(ClientOrderRepository clientOrderRepository, EmailService emailService) {
        this.clientOrderRepository = clientOrderRepository;
        this.emailService = emailService;
    }

    public List<ClientOrder> findAllClientOrders() {

        return clientOrderRepository.findAll();
    }

    public ClientOrder addClientOrder(ClientOrder clientOrder) throws InvalidClientOrderException {
        ClientOrder co = clientOrderRepository.save(clientOrder);

        if (co == null) throw new InvalidClientOrderException();

        EmailToSend email = EmailToSend.builder().to("order.manager@msn.com").subject("New Client Order").body("A new client order has been received with id " + clientOrder.getId()).build();
        emailService.sendMail(email);

        return co;
    }
}
