package com.soen390.erp.manufacturing;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.manufacturing.controller.BikeController;
import com.soen390.erp.manufacturing.exceptions.BikeNotFoundException;
import com.soen390.erp.manufacturing.model.Bike;
import com.soen390.erp.manufacturing.model.Handlebar;
import com.soen390.erp.manufacturing.repository.BikeRepository;
import com.soen390.erp.manufacturing.service.BikeModelAssembler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
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
public class BikeTest {
    @Autowired
    BikeController bikeController;

    @MockBean
    private BikeRepository bikeRepository;
    @MockBean
    private BikeModelAssembler assembler;



    @Test
    public void allTest()
    {
        List<EntityModel<Bike>> bikes = new ArrayList<>();

        doReturn(bikes).when(assembler).assembleToModel();

        ResponseEntity<?> result = bikeController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Bike b1 = new Bike();

        doReturn(Optional.of(b1)).when(bikeRepository).findById(id);

        ResponseEntity<?> result = bikeController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new BikeNotFoundException(id)).when(bikeRepository)
                .findById(id);

        BikeNotFoundException result =
                Assertions.assertThrows(BikeNotFoundException.class, () -> {
                    bikeController.one(id);
                });

        assertEquals("Could not find bike " + id, result
                .getMessage());
    }

    @Test
    public void newBikeTest()
    {
        Bike bike = mock(Bike.class);
        Handlebar handlebar = new Handlebar();

        EntityModel<Bike> entityModel =
                (EntityModel<Bike>)mock(EntityModel.class);
        Link link = spy(Link.class);

        doReturn(handlebar).when(bike).getHandlebar();

        doReturn(bike).when(bikeRepository).save(bike);
        doReturn(entityModel).when(assembler).toModel(bike);
        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = bikeController.newBike(bike);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }
}
