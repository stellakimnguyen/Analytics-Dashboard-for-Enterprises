package com.soen390.erp.inventory.controller;

import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.inventory.exceptions.InvalidClientOrderException;
import com.soen390.erp.inventory.model.ClientOrder;
import com.soen390.erp.inventory.service.ClientOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-orders")
public class ClientOrderController {

    private final ClientOrderService clientOrderService;
    private final LogService logService;
    private static final String category = "inventory";

    public ClientOrderController(ClientOrderService clientOrderService, LogService logService) {
        this.clientOrderService = clientOrderService;
        this.logService = logService;
    }

    @GetMapping
    public List<ClientOrder> getAllClientOrders() {
        logService.addLog("Retrieved all client order", category);
        return clientOrderService.findAllClientOrders();
    }

    @PostMapping
    public ResponseEntity<?> addClientOrder(@RequestBody ClientOrder clientOrder) {
        try {
            clientOrderService.addClientOrder(clientOrder);

        } catch (InvalidClientOrderException e) {
            logService.addLog("Failed to create client.", category);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        String message = "The Client Order was successfully created with id " + clientOrder.getId();
        logService.addLog(message, category);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}

