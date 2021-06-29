package com.soen390.erp.inventory.repository;

import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.manufacturing.model.Bike;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PlantBikeRepository extends PagingAndSortingRepository<PlantBike, Integer> {

    Optional<PlantBike> findByBike(Bike bike);

    public PlantBike findById(int id);

    public List<PlantBike> findAll();

    public PlantBike save(PlantBike plantBike);
}
