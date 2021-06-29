package com.soen390.erp.inventory.repository;


import com.soen390.erp.inventory.model.Plant;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends PagingAndSortingRepository<Plant, Integer> {
    public List<Plant> findAll();

}
