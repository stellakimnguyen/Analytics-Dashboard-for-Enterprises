package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.Ledger;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LedgerRepository extends PagingAndSortingRepository<Ledger, Integer> {
    public List<Ledger> findAll();

}
