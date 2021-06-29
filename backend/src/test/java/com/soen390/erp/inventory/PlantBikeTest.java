package com.soen390.erp.inventory;

import com.soen390.erp.inventory.controller.PlantBikeController;
import com.soen390.erp.inventory.exceptions.InvalidPlantBikeException;
import com.soen390.erp.inventory.model.PlantBike;
import com.soen390.erp.inventory.service.PlantBikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class PlantBikeTest {

    @Autowired
    private PlantBikeController plantBikeController;

    @MockBean
    private PlantBikeService plantBikeService;

    @Test
    public void getAllPlantBikesTest()
    {
        PlantBike s1 = new PlantBike();
        PlantBike s2 = new PlantBike();

        List<PlantBike> plantBikes = new ArrayList<>();
        plantBikes.add(s1);
        plantBikes.add(s2);

        doReturn(plantBikes).when(plantBikeService).findAllPlantBikes();

        List<PlantBike> result = plantBikeController.getAllPlantBikes();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void addPlantBikeTest()
    {
        PlantBike s1 = mock(PlantBike.class);

        doReturn(s1).when(plantBikeService).addPlantBike(s1);

        ResponseEntity<?> result = plantBikeController.addPlantBike(s1);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void addPlantBikeForbiddenTest()
    {
        PlantBike s1 = mock(PlantBike.class);

        doThrow(new InvalidPlantBikeException()).when(plantBikeService)
                .addPlantBike(s1);

        ResponseEntity<?> result = plantBikeController.addPlantBike(s1);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }
}
