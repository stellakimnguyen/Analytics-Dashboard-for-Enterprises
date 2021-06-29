package com.soen390.erp.manufacturing.repository;

import com.soen390.erp.manufacturing.model.Part;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PartRepository extends PagingAndSortingRepository<Part, Integer> {

    public List<Part> findAll();
}
