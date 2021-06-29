package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.model.PurchaseOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PurchaseOrderRepository extends PagingAndSortingRepository<PurchaseOrder, Integer> {
    public List<PurchaseOrder> findAll();
}
