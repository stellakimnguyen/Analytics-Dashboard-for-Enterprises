package com.soen390.erp.accounting.repository;

import com.soen390.erp.accounting.model.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClientRepository extends PagingAndSortingRepository<Client, Integer> {

    public List<Client> findAll();

    public Client findById(int id);

    public void deleteById(int id);
}