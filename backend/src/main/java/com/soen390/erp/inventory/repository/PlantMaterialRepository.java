package com.soen390.erp.inventory.repository;

import com.soen390.erp.inventory.model.PlantMaterial;
import com.soen390.erp.manufacturing.model.Material;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PlantMaterialRepository extends PagingAndSortingRepository<PlantMaterial, Integer> {

    Optional<PlantMaterial> findByMaterial(Material material);
}
