package com.soen390.erp.inventory.repository;

import com.soen390.erp.inventory.model.ClientOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClientOrderRepository extends PagingAndSortingRepository<ClientOrder, Integer> {

    public List<ClientOrder> findAll();
}
