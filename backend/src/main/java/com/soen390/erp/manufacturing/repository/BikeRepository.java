package com.soen390.erp.manufacturing.repository;

import com.soen390.erp.manufacturing.model.Bike;
import com.soen390.erp.manufacturing.model.Material;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BikeRepository extends PagingAndSortingRepository<Bike, Integer> {

    public List<Bike> findAll();
}
