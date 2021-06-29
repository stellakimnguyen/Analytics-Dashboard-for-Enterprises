package com.soen390.erp.manufacturing;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.manufacturing.controller.MaterialController;
import com.soen390.erp.manufacturing.exceptions.MaterialNotFoundException;
import com.soen390.erp.manufacturing.model.Material;
import com.soen390.erp.manufacturing.repository.MaterialRepository;
import com.soen390.erp.manufacturing.service.MaterialModelAssembler;
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
public class MaterialTest {
    @Autowired
    MaterialController materialController;

    @MockBean
    private MaterialRepository materialRepository;
    @MockBean
    private MaterialModelAssembler assembler;


    @Test
    public void allTest()
    {
        List<EntityModel<Material>> materials = new ArrayList<>();

        doReturn(materials).when(assembler).assembleToModel();

        ResponseEntity<?> result = materialController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Material m1 = new Material();

        doReturn(Optional.of(m1)).when(materialRepository).findById(id);

        ResponseEntity<?> result = materialController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new MaterialNotFoundException(id)).when(materialRepository)
                .findById(id);

        MaterialNotFoundException result =
                Assertions.assertThrows(MaterialNotFoundException.class, () -> {
                    materialController.one(id);
                });

        assertEquals("Could not find material" + id, result
                .getMessage());
    }

    @Test
    public void newMaterialTest()
    {
        Material m1 = mock(Material.class);

        EntityModel<Material> entityModel =
                (EntityModel<Material>)mock(EntityModel.class);
        Link link = mock(Link.class);

        doReturn(m1).when(materialRepository).save(m1);
        doReturn(entityModel).when(assembler).toModel(m1);
        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = materialController.newMaterial(m1);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void partNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                materialController.partNotFoundException(
                        new MaterialNotFoundException(id));

        assertEquals("Could not find material" + id, result);
    }

}
