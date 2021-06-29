package com.soen390.erp.inventory.repository;

import com.soen390.erp.inventory.model.Plant;
import com.soen390.erp.inventory.model.SupplierOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SupplierOrderRepository extends PagingAndSortingRepository<SupplierOrder, Integer> {
    public List<SupplierOrder> findAll();
}
