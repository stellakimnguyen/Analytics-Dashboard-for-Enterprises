package com.soen390.erp.accounting.service;

import com.soen390.erp.accounting.exceptions.SupplierNotFoundException;
import com.soen390.erp.accounting.model.Supplier;
import com.soen390.erp.accounting.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }


    public Supplier findSupplierById(int id) throws SupplierNotFoundException{

        if ( !supplierRepository.existsById(id) )
            throw new SupplierNotFoundException(id);

        return supplierRepository.findById(id);
    }


    public List<Supplier> findAllSuppliers() {

        return supplierRepository.findAll();
    }


    public Supplier saveSupplier(Supplier client) {
        return supplierRepository.save(client);
    }


    public boolean deleteSupplierById(int id) {

        if ( !supplierRepository.existsById(id) )
            return false;

        supplierRepository.deleteById(id);

        return true;
    }
}
