package com.soen390.erp.manufacturing.service;

import com.soen390.erp.manufacturing.controller.BikeController;
import com.soen390.erp.manufacturing.model.Bike;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.soen390.erp.manufacturing.repository.BikeRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BikeModelAssembler implements RepresentationModelAssembler<Bike,
        EntityModel<Bike>>
{
    private final BikeRepository bikeRepository;

    public BikeModelAssembler(BikeRepository bikeRepository)
    {
        this.bikeRepository = bikeRepository;
    }

    @Override
    public EntityModel<Bike> toModel(Bike bike) {
        return EntityModel.of(bike,
                linkTo(methodOn(BikeController.class).one(bike.getId()))
                        .withSelfRel(),
                linkTo(methodOn(BikeController.class).all()).withRel("bikes"));
    }

    public List<EntityModel<Bike>> assembleToModel() {
        return bikeRepository.findAll().stream()
                .map(this ::toModel)
                .collect(Collectors.toList());
    }
}
