package com.soen390.erp.accounting;

import com.soen390.erp.accounting.controller.SupplierController;
import com.soen390.erp.accounting.exceptions.InvalidSupplierException;
import com.soen390.erp.accounting.exceptions.SupplierNotFoundException;
import com.soen390.erp.accounting.model.Supplier;
import com.soen390.erp.accounting.service.SupplierService;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.service.EmailService;
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
public class SupplierTest {

    @Autowired
    private SupplierController supplierController;

    @MockBean
    private SupplierService supplierService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private LogService logService;

    @Test
    public void getAllSuppliersTest()
    {
        Supplier s1 = new Supplier();
        Supplier s2 = new Supplier();

        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(s1);
        suppliers.add(s2);

        doReturn(suppliers).when(supplierService).findAllSuppliers();

        List<Supplier> result = supplierController.getAllSuppliers();

        assertThat(result.size()).isEqualTo(2);
    }


    @Test
    public void findSupplierByIdTest()
    {
        int id = 1;
        Supplier s1 = new Supplier();
        doReturn(s1).when(supplierService).findSupplierById(id);

        ResponseEntity<?> result = supplierController.findSupplierById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void findSupplierByIdForbiddenTest()
    {
        int id = 1;
        String errorMessage = "Could not find supplier " + id;

        doThrow(new SupplierNotFoundException(id)).when(supplierService).findSupplierById(id);

        ResponseEntity<?> result = supplierController.findSupplierById(id);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

        assertEquals(errorMessage, result.getBody());
    }


    @Test
    public void addSupplierTest()
    {
        Supplier s1 = new Supplier();
        doReturn(s1).when(supplierService).saveSupplier(s1);

        ResponseEntity<?> result = supplierController.addSupplier(s1);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void addSupplierForbiddenTest()
    {
        Supplier s1 = new Supplier();

        doThrow(new InvalidSupplierException()).when(supplierService).saveSupplier(s1);

        ResponseEntity<?> result = supplierController.addSupplier(s1);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteSupplierByIdTest()
    {
        int id = 1;
        doReturn(true).when(supplierService).deleteSupplierById(id);

        ResponseEntity<?> result = supplierController.deleteSupplierById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteSupplierByIdNotFoundTest()
    {
        int id = 1;
        doReturn(false).when(supplierService).deleteSupplierById(id);

        ResponseEntity<?> result = supplierController.deleteSupplierById(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
