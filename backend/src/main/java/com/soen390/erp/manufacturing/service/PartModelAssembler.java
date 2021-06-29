package com.soen390.erp.manufacturing.service;

import com.soen390.erp.manufacturing.controller.PartController;
import com.soen390.erp.manufacturing.model.Material;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.soen390.erp.manufacturing.model.Part;
import com.soen390.erp.manufacturing.repository.MaterialRepository;
import com.soen390.erp.manufacturing.repository.PartRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class PartModelAssembler implements
        RepresentationModelAssembler<Part, EntityModel<Part>>
{

    private final PartRepository partRepository;

    public PartModelAssembler(PartRepository partRepository)
    {
        this.partRepository = partRepository;
    }

    @Override
    public EntityModel<Part> toModel(Part part) {
        return EntityModel.of(part,
                linkTo(methodOn(PartController.class).one(part.getId()))
                        .withSelfRel(),
                linkTo(methodOn(PartController.class).all()).withRel("parts"));
    }

    public List<EntityModel<Part>> assembleToModel() {
        return partRepository.findAll().stream()
                .map(this ::toModel)
                .collect(Collectors.toList());
    }
}
