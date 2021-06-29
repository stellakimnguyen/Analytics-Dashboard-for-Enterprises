package com.soen390.erp.inventory.service;

import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import com.soen390.erp.inventory.exceptions.PlantBikeNotFoundException;
import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.inventory.repository.PlantBikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantBikeService {

    private final PlantBikeRepository plantBikeRepository;
    private final EmailService emailService;

    public PlantBikeService(PlantBikeRepository plantBikeRepository, EmailService emailService) {
        this.plantBikeRepository = plantBikeRepository;
        this.emailService = emailService;
    }

    public PlantBike findPlantBikeById(int id) throws PlantBikeNotFoundException {

        if (!plantBikeRepository.existsById(id))
            throw new PlantBikeNotFoundException(id);

        return plantBikeRepository.findById(id);
    }


    public List<PlantBike> findAllPlantBikes() {
        return plantBikeRepository.findAll();
    }

    public PlantBike addPlantBike(PlantBike plantBike) {
        EmailToSend email = EmailToSend.builder().to("plant.manager@msn.com").subject("New Plant Bike").body("A new plant bike has been added with id " + plantBike.getId()).build();
        emailService.sendMail(email);
        return plantBikeRepository.save(plantBike);
    }

    public boolean deletePlantBikeById(int id) {

        if (!plantBikeRepository.existsById(id))
            return false;

        plantBikeRepository.deleteById(id);

        return true;
    }
}
