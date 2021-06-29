package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.Supplier;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SupplierRepository extends PagingAndSortingRepository<Supplier, Integer> {

    public List<Supplier> findAll();

    public Supplier findById(int id);

    public void deleteById(int id);
}