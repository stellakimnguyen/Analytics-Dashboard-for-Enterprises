package com.soen390.erp.inventory.controller;

import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.inventory.exceptions.InvalidPlantBikeException;
import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.inventory.service.PlantBikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plant-bikes")
public class PlantBikeController {

    private final PlantBikeService plantBikeService;
    private final LogService logService;
    private static final String category = "inventory";

    public PlantBikeController(PlantBikeService plantBikeService, LogService logService) {
        this.plantBikeService = plantBikeService;
        this.logService = logService;
    }

    @GetMapping
    public List<PlantBike> getAllPlantBikes() {
        logService.addLog("Retrieved all plant bikes.", category);
        return plantBikeService.findAllPlantBikes();
    }

    @PostMapping
    public ResponseEntity<?> addPlantBike(@RequestBody PlantBike plantBike) {
        try {
            plantBikeService.addPlantBike(plantBike);

        } catch (InvalidPlantBikeException e) {
            logService.addLog("Failed to create plantBike.", category);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        String message = "The Plant Bike was successfully added with id " + plantBike.getId();
        logService.addLog(message, category);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
