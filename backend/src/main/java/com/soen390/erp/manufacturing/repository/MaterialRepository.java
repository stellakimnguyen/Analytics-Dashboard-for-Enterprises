package com.soen390.erp.manufacturing.repository;

import com.soen390.erp.manufacturing.model.Material;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MaterialRepository extends PagingAndSortingRepository<Material, Integer> {

    public List<Material> findAll();
}
