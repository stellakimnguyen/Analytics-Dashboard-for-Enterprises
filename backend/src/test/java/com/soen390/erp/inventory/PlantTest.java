package com.soen390.erp.inventory;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.inventory.controller.PlantController;
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
import com.soen390.erp.manufacturing.model.Bike;
import com.soen390.erp.manufacturing.model.Material;
import com.soen390.erp.manufacturing.model.Part;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class PlantTest {

    @Autowired
    private PlantController plantController;

    @MockBean
    PlantRepository plantRepository;
    @MockBean
    PlantModelAssembler pmAssembler;
    @MockBean
    PlantService plantService;

    @Test
    public void getAllPlantsTest()
    {
        List<EntityModel<Plant>> plants = new ArrayList<>();

        doReturn(plants).when(pmAssembler).assembleToModel();

        ResponseEntity<?> result = plantController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Plant plant = mock(Plant.class);

        doReturn(Optional.of(plant)).when(plantRepository).findById(id);

        ResponseEntity<?> result = plantController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new PlantNotFoundException(id)).when(plantRepository)
                .findById(id);

        PlantNotFoundException result =
                Assertions.assertThrows(PlantNotFoundException.class, () -> {
                    plantController.one(id);
                });

        assertEquals("Could not find Plant" + id, result
                .getMessage());
    }

    @Test
    public void addPartToInventoryTest()
    {
        int id = 1 ;
        int quantity = 1;
        PlantPart plantPart = mock(PlantPart.class);
        Plant plant = mock(Plant.class);
        Part part = mock(Part.class);

        doReturn(Optional.of(plant)).when(plantRepository).findById(id);
        doReturn(part).when(plantPart).getPart();
        doReturn(quantity).when(plantPart).getQuantity();

        ResponseEntityWrapper result = plantController.addPartToInventory(plantPart);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void addPartToInventoryPlantNotFoundTest()
    {
        int id = 1 ;
        PlantPart plantPart = mock(PlantPart.class);

        doThrow(new PlantNotFoundException(id)).when(plantRepository)
                .findById(id);

        PlantNotFoundException result =
                Assertions.assertThrows(PlantNotFoundException.class, () -> {
                    plantController.addPartToInventory(plantPart);
                });

        assertEquals("Could not find Plant" + id, result
                .getMessage());
    }

    @Test
    public void partNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                plantController.partNotFoundException(
                        new PlantNotFoundException(id));

        assertEquals("Could not find Plant" + id, result);
    }

    @Test
    public void materialNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                plantController.materialNotFoundException(
                        new MaterialNotFoundException(id));

        assertEquals("Could not find material" + id, result);
    }

    @Test
    public void notEnoughMaterialInPlantExceptionTest()
    {
        String result =
                plantController.notEnoughMaterialInPlantException(
                        new NotEnoughMaterialInPlantException("Not enough " +
                                "materials in plant"));

        assertEquals("Not enough " +
                "materials in plant", result);
    }

    @Test
    public void notEnoughPartsInPlantExceptionTest()
    {
        String result =
                plantController.notEnoughPartsInPlantException(
                        new NotEnoughPartsInPlantException("Not enough " +
                                "parts in plant"));

        assertEquals("Not enough " +
                "parts in plant", result);
    }

    @Test
    public void addMaterialToInventoryTest()
    {
        int id = 1 ;
        int quantity = 1;
        PlantMaterial plantMaterial = mock(PlantMaterial.class);
        Material material = mock(Material.class);
        Plant plant = mock(Plant.class);
        Part part = mock(Part.class);

        doReturn(Optional.of(plant)).when(plantRepository).findById(id);
        doReturn(material).when(plantMaterial).getMaterial();
        doReturn(quantity).when(plantMaterial).getQuantity();

        ResponseEntityWrapper result = plantController.addMaterialToInventory(plantMaterial);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void addMaterialToInventoryPlantNotFoundTest()
    {
        int id = 1 ;
        PlantMaterial plantMaterial = mock(PlantMaterial.class);

        doThrow(new PlantNotFoundException(id)).when(plantRepository)
                .findById(id);

        PlantNotFoundException result =
                Assertions.assertThrows(PlantNotFoundException.class, () -> {
                    plantController.addMaterialToInventory(plantMaterial);
                });

        assertEquals("Could not find Plant" + id, result
                .getMessage());
    }

    @Test
    public void addBikeToInventoryTest()
    {
        int id = 1 ;
        int quantity = 1;
        PlantBike plantBike = mock(PlantBike.class);
        Bike bike = mock(Bike.class);
        Plant plant = mock(Plant.class);
        Part part = mock(Part.class);

        doReturn(Optional.of(plant)).when(plantRepository).findById(id);
        doReturn(bike).when(plantBike).getBike();
        doReturn(quantity).when(plantBike).getQuantity();

        ResponseEntityWrapper result = plantController.addBikeToInventory(plantBike);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void addBikeToInventoryPlantNotFoundTest()
    {
        int id = 1 ;
        PlantBike plantBike = mock(PlantBike.class);

        doThrow(new PlantNotFoundException(id)).when(plantRepository)
                .findById(id);

        PlantNotFoundException result =
                Assertions.assertThrows(PlantNotFoundException.class, () -> {
                    plantController.addBikeToInventory(plantBike);
                });

        assertEquals("Could not find Plant" + id, result
                .getMessage());
    }
}
