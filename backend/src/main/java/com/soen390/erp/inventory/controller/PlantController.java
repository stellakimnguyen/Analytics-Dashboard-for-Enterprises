package com.soen390.erp.inventory.controller;


import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.inventory.exceptions.NotEnoughMaterialInPlantException;
import com.soen390.erp.inventory.exceptions.NotEnoughPartsInPlantException;
import com.soen390.erp.inventory.exceptions.PlantNotFoundException;
import com.soen390.erp.inventory.model.Plant;
import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.inventory.model.PlantMaterial;
import com.soen390.erp.inventory.model.PlantPart;
import com.soen390.erp.inventory.repository.PlantRepository;
import com.soen390.erp.inventory.service.PlantModelAssembler;
import com.soen390.erp.inventory.service.PlantService;
import com.soen390.erp.manufacturing.exceptions.MaterialNotFoundException;
import com.soen390.erp.manufacturing.exceptions.PartNotFoundException;
import com.soen390.erp.manufacturing.model.Bike;
import com.soen390.erp.manufacturing.model.Material;
import com.soen390.erp.manufacturing.model.Part;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// All plants created
// All information in a plant (materials, parts, bikes) or maybe have orders?

@RestController

public class PlantController {
    private final PlantRepository plantRepository;
    private final PlantModelAssembler pmAssembler;
    private final PlantService plantService;
    private final LogService logService;
    private static final String category = "inventory";

    public PlantController(PlantRepository plantRepository, PlantModelAssembler pmAssembler, PlantService plantService, LogService logService){
        this.plantRepository = plantRepository;
        this.pmAssembler = pmAssembler;
        this.plantService = plantService;
        this.logService = logService;
    }

    @GetMapping("/plants")
    public ResponseEntity<?> all()
    {
        List<EntityModel<Plant>> plants = pmAssembler.assembleToModel();
        logService.addLog("Retrieved all plants.", category);
        return ResponseEntity.ok().body(
                CollectionModel.of(plants, linkTo(methodOn(this.getClass()).all()).withSelfRel()));
    }

    @GetMapping("/plants/{id}")
    public ResponseEntity<?> one(@PathVariable int id){
        Plant plant = plantRepository.findById(id).orElseThrow(() -> new PlantNotFoundException(id));
        logService.addLog("Retrieved plant with id "+plant.getId()+".", category);
        return ResponseEntity.ok().body(pmAssembler.toModel(plant));
    }


    @PostMapping("/addPartToInventory")
    public ResponseEntityWrapper addPartToInventory(@RequestBody PlantPart plantPart) {

        int plantId = 1;
        Plant plant =
                plantRepository.findById(1).orElseThrow(()->new PlantNotFoundException(plantId));
        Part part = plantPart.getPart();
        int quantity = plantPart.getQuantity();
        plantService.addPlantPart(plant, part, quantity);
        //return ResponseEntity.status(HttpStatus.CREATED).build();
        return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).build()
                , "The Part with id "+ part.getId() + " was successfully added to the plant with id " + plantId);
    }

    @ResponseBody
    @ExceptionHandler(PartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String partNotFoundException(PlantNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MaterialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String materialNotFoundException(MaterialNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotEnoughMaterialInPlantException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notEnoughMaterialInPlantException(NotEnoughMaterialInPlantException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotEnoughPartsInPlantException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notEnoughPartsInPlantException(NotEnoughPartsInPlantException ex){
        return ex.getMessage();
    }

    @PostMapping("/addMaterialToInventory")
    public ResponseEntityWrapper addMaterialToInventory(@RequestBody PlantMaterial plantMaterial) {
        //TODO: get from header?
        int plantId = 1;

        Plant plant =
                plantRepository.findById(plantId).orElseThrow(()-> new PlantNotFoundException(plantId));
        Material material = plantMaterial.getMaterial();
        int quantity = plantMaterial.getQuantity();
        plantService.addPlantMaterial(plant,material,quantity);

        return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).build()
                , "The Material with id "+ material.getId() + " was successfully added to the plant with id " + plantId);
    }

    @PostMapping("/addBikeToInventory")
    public ResponseEntityWrapper addBikeToInventory(@RequestBody PlantBike plantBike) {
        //TODO: get from header?
        int plantId = 1;

        Plant plant =
                plantRepository.findById(plantId).orElseThrow(()-> new PlantNotFoundException(plantId));
        Bike bike = plantBike.getBike();
        int quantity = plantBike.getQuantity();
        plantService.addPlantBike(plant,bike,quantity);

        return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).build()
                , "The Bike with id "+ bike.getId() + " was successfully added to the plant with id " + plantId);
    }

}

