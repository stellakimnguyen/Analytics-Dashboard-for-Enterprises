package com.soen390.erp.inventory.service;

import com.soen390.erp.configuration.model.BooleanWrapper;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import com.soen390.erp.inventory.exceptions.NotEnoughMaterialInPlantException;
import com.soen390.erp.inventory.exceptions.NotEnoughPartsInPlantException;
import com.soen390.erp.inventory.model.Plant;
import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.inventory.model.PlantMaterial;
import com.soen390.erp.inventory.model.PlantPart;
import com.soen390.erp.inventory.repository.PlantBikeRepository;
import com.soen390.erp.inventory.repository.PlantMaterialRepository;
import com.soen390.erp.inventory.repository.PlantPartRepository;
import com.soen390.erp.inventory.repository.PlantRepository;
import com.soen390.erp.manufacturing.exceptions.PartNotFoundException;
import com.soen390.erp.manufacturing.model.Bike;
import com.soen390.erp.manufacturing.model.Handlebar;
import com.soen390.erp.manufacturing.model.Material;
import com.soen390.erp.manufacturing.model.Part;
import com.soen390.erp.manufacturing.repository.BikeRepository;
import com.soen390.erp.manufacturing.repository.MaterialRepository;
import com.soen390.erp.manufacturing.repository.PartRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final PlantMaterialRepository plantMaterialRepository;
    private final PlantPartRepository plantPartRepository;
    private final PlantBikeRepository plantBikeRepository;
    private final PartRepository partRepository;
    private final MaterialRepository materialRepository;
    private final BikeRepository bikeRepository;
    private final EmailService emailService;

    public PlantService(PlantRepository plantRepository, PlantMaterialRepository plantMaterialRepository, PlantPartRepository plantPartRepository, PlantBikeRepository plantBikeRepository, PartRepository partRepository, MaterialRepository materialRepository, BikeRepository bikeRepository, EmailService emailService) {
        this.plantRepository = plantRepository;
        this.plantMaterialRepository = plantMaterialRepository;
        this.plantPartRepository = plantPartRepository;
        this.plantBikeRepository = plantBikeRepository;
        this.partRepository = partRepository;
        this.materialRepository = materialRepository;
        this.bikeRepository = bikeRepository;
        this.emailService = emailService;
    }
    public void addPlantMaterial(Plant plant, Material material, int quantity) {

        Material material1 = materialRepository.findById(material.getId()).orElseThrow(()->new PartNotFoundException(material.getId()));
        materialRepository.save(material1);
        //try to find the pm from the pm repository
        PlantMaterial pm = plantMaterialRepository.findByMaterial(material1)
                .orElseGet(() -> PlantMaterial.builder().material(material1).build());

        //increase the quantity
        pm.setQuantity(pm.getQuantity() + quantity);
        plantMaterialRepository.save(pm);

        plant.addMaterial(pm);
        plantRepository.save(plant);
    }

    public void addPlantPart(Plant plant, Part part, int quantity) {

        partRepository.findById(part.getId()).orElseThrow(()->new PartNotFoundException(part.getId()));

        Set<PlantPart> parts = plant.getParts().orElseGet(() -> new HashSet<>());
        Set<Material> materials = part.getMaterials().orElseGet(() -> new HashSet<>());

        Stream<PlantMaterial> materialsInPlantForPart = materials.stream()
                .map(plantMaterialRepository::findByMaterial)
                .filter(Optional::isPresent)
                .map(Optional::get);


        Set<PlantMaterial> materialsInPlant = materialsInPlantForPart.collect(Collectors.toSet());
        if (materialsInPlant.size()!=materials.size()) {
            EmailToSend email = EmailToSend.builder().to("material.manager@msn.com").subject("test").body("Restock the inventory!").build();
            emailService.sendMail(email);
            throw new NotEnoughMaterialInPlantException("Please ensure all the materials are present.");
        }


        Set<PlantMaterial> pm = materialsInPlant.stream()
                .map(plantMaterial -> PlantMaterial.builder()
                        .id(plantMaterial.getId())
                        .material(plantMaterial.getMaterial())
                        .quantity(plantMaterial.getQuantity() - quantity)
                        .build())
                .collect(Collectors.toSet());

        //check if the quantity of matrerial has gone below 0
        pm.forEach(plantMaterial -> {
            if (plantMaterial.getQuantity()<0) {
                EmailToSend email = EmailToSend.builder().to("material.manager@msn.com").subject("Material Inventory Stock Low").body("Restock the inventory!").build();
                emailService.sendMail(email);
                throw new NotEnoughMaterialInPlantException("Not enough material with id " + plantMaterial.getMaterial().getId());
            }
        });
        //otherwise save
        pm.forEach(plantMaterialRepository::save);

        PlantPart plantPart = plantPartRepository.findByPart(part)
                .orElseGet(()->PlantPart.builder().part(part).build());

        plantPart.setQuantity(plantPart.getQuantity()+quantity);
        parts.add(plantPart);
        plant.setParts(parts);
        plantPartRepository.save(plantPart);
        plantRepository.save(plant);
    }

    public void addPlantBike(Plant plant, Bike bike, int quantity){

        //check if the bike exists in the repo, if not add it
        bikeRepository.save(bikeRepository.findById(bike.getId()).orElse(bike));

        //get all the parts of the bikes and extract it to a set
        Set<Part> bikeParts = Set.of(bike.getFrame(), bike.getFrontwheel(), bike.getHandlebar(), bike.getRearwheel(),
                bike.getPedal(), bike.getSeat());

        Stream<PlantPart> plantPartInInventory = bikeParts.stream()
                .map(plantPartRepository::findByPart)
                .filter(Optional::isPresent)
                .map(Optional::get);

        Set<PlantPart> partsInPlant = plantPartInInventory.collect(Collectors.toSet());

        //TODO: Make this to return the specific parts not available
        if (partsInPlant.size()!=bikeParts.size()){
            EmailToSend email = EmailToSend.builder().to("part.manager@msn.com").subject("Material Inventory Stock Low").body("Restock the inventory!").build();
            emailService.sendMail(email);
            throw new NotEnoughPartsInPlantException("Please ensure all the parts are present.");
        }


        Set<PlantPart> pp = partsInPlant.stream()
                .map(plantPart -> PlantPart.builder()
                        .id(plantPart.getId())
                        .part(plantPart.getPart())
                        .quantity(plantPart.getQuantity() - quantity)
                        .build())
                .collect(Collectors.toSet());

        //check if the quantity of part has gone below 0
        pp.forEach(plantPart -> {
            if (plantPart.getQuantity()<0) {
                String ex = "Not enough parts with id " + plantPart.getPart().getId();
                EmailToSend email = EmailToSend.builder().to("material.manager@msn.com").subject("Material Inventory Stock Low").body(ex).build();
                emailService.sendMail(email);
                throw new NotEnoughMaterialInPlantException(ex);
            }
        });
        //otherwise save
        pp.forEach(plantPartRepository::save);

        PlantBike plantBike = plantBikeRepository.findByBike(bike)
                .orElseGet(()->PlantBike.builder().bike(bike).build());
        //increment the quantity
        plantBike.setQuantity(plantBike.getQuantity()+quantity);
        plantBikeRepository.save(plantBike);

        plant.addBike(plantBike);
        plantRepository.save(plant);

    }

    public void removePlantBike(Plant plant, Bike bike, int quantity){
        //TODO: consider fetching plant from the controller when validation takes place and pass it
        Plant plantInDb = plantRepository.findById(plant.getId()).get();
        Set<PlantBike> plantBikes = plantInDb.getBikes().get();

        for (PlantBike x : plantBikes){
            if (x.getBike().getId() == bike.getId()){
                x.setQuantity(x.getQuantity() - quantity);
                break;
            }
        }
        plantInDb.setBikes(plantBikes);
        plantRepository.save(plantInDb);
    }

    public BooleanWrapper checkSufficientParts(Plant plant, Bike bike, int quantity) {
        //TODO: return all strings for each part.
        Set<PlantPart> parts = plant.getParts().get();
        String message = "";
        boolean handlebarCheck = false, frameCheck = false, frontwheelCheck = false, rearwheelCheck= false, pedalCheck = false, seatCheck = false;
        // PlantPart pp = parts.iterator().next();
        Set<PlantPart> parts2 = Set.copyOf(parts);
        for (PlantPart pps : parts2){
            if(pps.getPart().getId() == bike.getHandlebar().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        // return new BooleanWrapper(false, "Missing " + result + " handlebars with id " + pps.getPart().getId() + " to complete the order.");
                        int result = quantity - pps.getQuantity();
                        message += "Missing " + result + " handlebars with id " + pps.getPart().getId() + " to complete the order. \n\r ";
                        message += "Attempted to create handlebars but not enough materials. \n\r ";
                        continue;
                    }
                }
                message += "All handlebars are present to create the bikes. \n\r ";
                handlebarCheck = true;
                continue;
            }
            if(pps.getPart().getId() == bike.getFrame().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        //int result = quantity - pps.getQuantity();
                        //return new BooleanWrapper(false, "Missing " + result + " frames with id " + pps.getPart().getId() + " to complete the order.");
                        int result = quantity - pps.getQuantity();
                        message += "Missing " + result + " frames with id " + pps.getPart().getId() + " to complete the order. \n\r ";
                        message += "Attempted to create handlebars but not enough materials.\n\r ";
                        continue;
                    }
                }
                message += "All frames needed to create the bikes are present.\n\r ";
                frameCheck = true;
                continue;
            }
            if(pps.getPart().getId() == bike.getFrontwheel().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        int result = quantity - pps.getQuantity();
                        //return new BooleanWrapper(false, "Missing " + result + " frontwheels with id " + pps.getPart().getId() + " to complete the order.");
                        message += "Missing " + result + " front wheels with id " + pps.getPart().getId() + " to complete the order.\n\r ";
                        message += "Attempted to create front wheels but not enough materials.\n\r ";
                        continue;
                    }
                }
                message += "All front wheels needed to create the bikes are present.\n\r";
                frontwheelCheck = true;
                continue;
            }
            if(pps.getPart().getId() == bike.getRearwheel().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        int result = quantity - pps.getQuantity();
                        message += "Missing " + result + " rear wheels with id " + pps.getPart().getId() + " to complete the order.\n\r";
                        message += "Attempted to create rear wheels but not enough materials.\n\r";
                        continue;
                        // return new BooleanWrapper(false, "Missing " + result + " rearwheels with id " + pps.getPart().getId() + " to complete the order.");
                    }
                }
                message += "All rear wheels needed to create the bikes are present.\n\r";
                rearwheelCheck = true;
                continue;
            }
            if(pps.getPart().getId() == bike.getPedal().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        int result = quantity - pps.getQuantity();
                        // return new BooleanWrapper(false, "Missing " + result + " pedals with id " + pps.getPart().getId() + " to complete the order.");
                        message += "Missing " + result + " pedals with id " + pps.getPart().getId() + " to complete the order.\n\r ";
                        message += "Attempted to create pedals but not enough materials.\n\r ";
                        continue;
                    }
                }
                message += "All pedals needed to create the bikes are present.\n\r ";
                pedalCheck = true;
                continue;
            }
            if(pps.getPart().getId() == bike.getSeat().getId()){
                if(pps.getQuantity() < quantity){
                    try {
                        addPlantPart(plant,pps.getPart(), quantity - pps.getQuantity());
                        //TODO: notify that part was made because material was sufficient
                    } catch (NotEnoughMaterialInPlantException ex) {
                        int result = quantity - pps.getQuantity();
                        message += "Missing " + result + " seats with id " + pps.getPart().getId() + " to complete the order.\n\r ";
                        message += "Attempted to create seats but not enough materials.\n\r ";
                        continue;
                        //return new BooleanWrapper(false, "Missing " + result + " seats with id " + pps.getPart().getId() + " to complete the order.");
                    }
                }
                message += "All seats needed to create the bikes are present.\n\r ";
                seatCheck = true;
                continue;
            }
        }
        if(handlebarCheck && frameCheck && frontwheelCheck && rearwheelCheck && pedalCheck && seatCheck) {
            return new BooleanWrapper(true, "All parts needed to make the bikes are present.");
        }
        else{
            return new BooleanWrapper(false, message);
        }
        
    }

}
