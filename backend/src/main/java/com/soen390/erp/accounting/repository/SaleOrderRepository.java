package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.SaleOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface SaleOrderRepository extends PagingAndSortingRepository<SaleOrder, Integer> {
    public SaleOrder save(SaleOrder saleOrder);

    public ArrayList<SaleOrder> findAll();

    public Optional<SaleOrder> findById(int id) ;
}
