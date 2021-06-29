package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {
    public List<Account> findAll();

}