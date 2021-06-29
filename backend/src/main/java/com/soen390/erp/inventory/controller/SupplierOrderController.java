package com.soen390.erp.inventory.controller;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.inventory.model.SupplierOrder;
import com.soen390.erp.inventory.service.SupplierOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SupplierOrderController {

    @Autowired
    private SupplierOrderService supplierOrderservice;
    @Autowired
    private LogService logService;
    private static final String category = "inventory";

    @GetMapping(path = "/SupplierOrders")
    public List<SupplierOrder> getAllSupplierOrders(){
        logService.addLog("Retrieved all supplier orders.", category);
        return supplierOrderservice.getAllSupplierOrders();
    }

    @PostMapping(path = "/SupplierOrders")
    public ResponseEntityWrapper createSupplierOrder(@RequestBody SupplierOrder supplierOrder){
        //TODO: validate input
        boolean isSuccessful = supplierOrderservice.insertSupplierOrder(supplierOrder);
        if (isSuccessful == true){
            logService.addLog("Created supplier order with id ."+ supplierOrder.getId(), category);
            return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).body(supplierOrder.getId())
                    , "The Supplier Order was successfully added with id " + supplierOrder.getId()) ;
        }else{
            logService.addLog("Failed to create supplier order.", category);
            return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
                    ,"The Supplier Order could not be created");
        }
    }
}
