package com.soen390.erp.accounting.controller;

import com.soen390.erp.accounting.exceptions.SupplierNotFoundException;
import com.soen390.erp.accounting.exceptions.InvalidSupplierException;
import com.soen390.erp.accounting.model.Supplier;
import com.soen390.erp.accounting.service.SupplierService;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;
    private final EmailService emailService;
    private final LogService logService;
    private static final String category = "accounting";

    public SupplierController(SupplierService supplierService, EmailService emailService, LogService logRepository) {
        this.supplierService = supplierService;
        this.emailService = emailService;
        this.logService = logRepository;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {

        logService.addLog("Retrieved all suppliers.", category);
        return supplierService.findAllSuppliers();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> findSupplierById(@PathVariable int id) {

        Supplier c;
        try {
            c = supplierService.findSupplierById(id);
            logService.addLog("Retrieved supplier with id "+id+".", category);

        } catch (SupplierNotFoundException e) {
            logService.addLog("Failed to retrieved supplier with id "+id+". No such supplier exists.", category);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.ok().body(c);
    }


    @PostMapping
    public ResponseEntity<?> addSupplier(@RequestBody Supplier supplier) {
        try {
            supplierService.saveSupplier(supplier);

        } catch (InvalidSupplierException e) {
            logService.addLog("Failed to create supplier.", category);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        String message = "A new Supplier has been found with id " + supplier.getId() + ".";
        EmailToSend email = EmailToSend.builder().to("warehouse.manager@msn.com").subject("New Supplier").body(message).build();
        emailService.sendMail(email);
        logService.addLog(message, category);

        return ResponseEntity.status(HttpStatus.CREATED).body("Created Supplier with id " + supplier.getId() + ".");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteSupplierById(@PathVariable int id) {

        boolean isRemoved = supplierService.deleteSupplierById(id);

        if (!isRemoved) {
            logService.addLog("Failed to delete supplier with id "+id+".", category);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier Not Found by ID " + id);
        }

        logService.addLog("Deleted supplier with id "+id+".", category);
        return ResponseEntity.status(HttpStatus.OK).body("Supplier Deleted");
    }

}
