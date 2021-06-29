package com.soen390.erp.inventory;

import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.inventory.controller.SupplierOrderController;
import com.soen390.erp.inventory.model.SupplierOrder;
import com.soen390.erp.inventory.service.SupplierOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class SupplierOrderTest {

    @Autowired
    private SupplierOrderController supplierOrderController;

    @MockBean
    private SupplierOrderService supplierOrderservice;

    @Test
    public void getAllSupplierOrdersTest()
    {
        SupplierOrder s1 = new SupplierOrder();
        SupplierOrder s2 = new SupplierOrder();

        List<SupplierOrder> supplierOrders = new ArrayList<>();
        supplierOrders.add(s1);
        supplierOrders.add(s2);

        doReturn(supplierOrders).when(supplierOrderservice).getAllSupplierOrders();

        List<SupplierOrder> result = supplierOrderController.getAllSupplierOrders();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void createSupplierOrderTest()
    {
        SupplierOrder s1 = mock(SupplierOrder.class);

        doReturn(true).when(supplierOrderservice).insertSupplierOrder(s1);

        ResponseEntityWrapper result = supplierOrderController.createSupplierOrder(s1);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void createSupplierOrderForbiddenTest()
    {
        SupplierOrder s1 = mock(SupplierOrder.class);

        doReturn(false).when(supplierOrderservice).insertSupplierOrder(s1);

        ResponseEntityWrapper result = supplierOrderController.createSupplierOrder(s1);

        assertEquals(HttpStatus.FORBIDDEN, result.getResponseEntity().getStatusCode());
    }
}
