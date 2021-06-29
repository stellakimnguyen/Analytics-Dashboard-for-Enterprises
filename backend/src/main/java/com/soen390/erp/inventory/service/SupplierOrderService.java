package com.soen390.erp.inventory.service;

import com.soen390.erp.inventory.model.SupplierOrder;
import com.soen390.erp.inventory.repository.SupplierOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierOrderService {

    SupplierOrderRepository repository;

    @Autowired
    public SupplierOrderService(SupplierOrderRepository repository) {
        this.repository = repository;
    }

    public List<SupplierOrder> getAllSupplierOrders(){
        return repository.findAll();
    }

    public boolean insertSupplierOrder(SupplierOrder supplierOrder) {
        repository.save(supplierOrder);
        if (supplierOrder.getId() != 0){
            return true;
        }else{
            return false;
        }
        //TODO: verify insertion and return a response
    }
}
