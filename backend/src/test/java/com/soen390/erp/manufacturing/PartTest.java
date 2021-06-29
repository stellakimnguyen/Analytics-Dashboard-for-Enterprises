package com.soen390.erp.manufacturing;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.manufacturing.controller.PartController;
import com.soen390.erp.manufacturing.exceptions.PartNotFoundException;
import com.soen390.erp.manufacturing.model.Material;
import com.soen390.erp.manufacturing.model.Part;
import com.soen390.erp.manufacturing.repository.MaterialRepository;
import com.soen390.erp.manufacturing.repository.PartRepository;
import com.soen390.erp.manufacturing.service.PartModelAssembler;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class PartTest {
    @Autowired
    PartController partController;

    @MockBean
    private MaterialRepository materialRepository;
    @MockBean
    private PartModelAssembler assembler;
    @MockBean
    private PartRepository partRepository;


    @Test
    public void allTest()
    {
        List<EntityModel<Material>> materials = new ArrayList<>();

        doReturn(materials).when(assembler).assembleToModel();

        ResponseEntity<?> result = partController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Part part = mock(Part.class);

        doReturn(Optional.of(part)).when(partRepository).findById(id);

        ResponseEntity<?> result = partController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new PartNotFoundException(id)).when(partRepository)
                .findById(id);

        PartNotFoundException result =
                Assertions.assertThrows(PartNotFoundException.class, () -> {
                    partController.one(id);
                });

        assertEquals("Could not find the part "+ id, result
                .getMessage());
    }

    @Test
    public void newPartTest()
    {
        Part part = mock(Part.class);
        Set<Material> materials = mock(Set.class);

        EntityModel<Part> entityModel =
                (EntityModel<Part>)mock(EntityModel.class);
        Link link = mock(Link.class);

        doReturn(Optional.of(materials)).when(part).getMaterials();
        doReturn(part).when(partRepository).save(part);
        doReturn(entityModel).when(assembler).toModel(part);

        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = partController.newPart(part);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void newPartMaterialsEmptyTest()
    {
        Part part = mock(Part.class);

        EntityModel<Part> entityModel =
                (EntityModel<Part>)mock(EntityModel.class);
        Link link = mock(Link.class);

        doReturn(Optional.empty()).when(part).getMaterials();
        doReturn(part).when(partRepository).save(part);
        doReturn(entityModel).when(assembler).toModel(part);

        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = partController.newPart(part);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void partNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                partController.partNotFoundException(
                        new PartNotFoundException(id));

        assertEquals("Could not find the part "+ id, result);
    }
}
