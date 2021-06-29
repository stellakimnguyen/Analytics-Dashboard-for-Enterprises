package com.soen390.erp.inventory.repository;

import com.soen390.erp.inventory.model.PlantPart;
import com.soen390.erp.manufacturing.model.Part;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PlantPartRepository extends PagingAndSortingRepository<PlantPart, Integer> {

    Optional<PlantPart> findByPart(Part part);
}
