package com.soen390.erp.manufacturing.service;

import com.soen390.erp.manufacturing.controller.MaterialController;
import com.soen390.erp.manufacturing.model.Material;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.soen390.erp.manufacturing.repository.MaterialRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class MaterialModelAssembler implements
        RepresentationModelAssembler<Material, EntityModel<Material>>
{
    private final MaterialRepository materialRepository;

    public MaterialModelAssembler(MaterialRepository materialRepository)
    {
        this.materialRepository = materialRepository;
    }

    @Override
    public EntityModel<Material> toModel(Material material) {
        return EntityModel.of(material,
                linkTo(methodOn(MaterialController.class).one(material
                        .getId())).withSelfRel(),
                linkTo(methodOn(MaterialController.class).all())
                        .withRel("materials"));
    }

    public List<EntityModel<Material>> assembleToModel() {
        return materialRepository.findAll().stream()
                .map(this ::toModel)
                .collect(Collectors.toList());
    }
}
