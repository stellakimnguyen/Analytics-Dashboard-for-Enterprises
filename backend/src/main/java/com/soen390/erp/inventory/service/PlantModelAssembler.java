package com.soen390.erp.inventory.service;

import com.soen390.erp.inventory.controller.PlantController;
import com.soen390.erp.inventory.model.Plant;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.soen390.erp.inventory.repository.PlantRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class PlantModelAssembler implements RepresentationModelAssembler<Plant,
        EntityModel<Plant>>
{
    private final PlantRepository plantRepository;

    public PlantModelAssembler(PlantRepository plantRepository)
    {
        this.plantRepository = plantRepository;
    }

    public EntityModel<Plant> toModel(Plant plant){
        return EntityModel.of(plant,
                linkTo(methodOn(PlantController.class).one(plant.getId())).withSelfRel(),
                linkTo(methodOn(PlantController.class).all()).withRel("plants"));
    }

    public List<EntityModel<Plant>> assembleToModel() {
        return plantRepository.findAll().stream()
                .map(this ::toModel)
                .collect(Collectors.toList());
    }
}
