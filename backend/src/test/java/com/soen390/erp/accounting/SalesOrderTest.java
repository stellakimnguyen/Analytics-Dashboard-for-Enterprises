package com.soen390.erp.accounting;

import com.soen390.erp.accounting.controller.SaleOrderController;
import com.soen390.erp.accounting.model.SaleOrder;
import com.soen390.erp.accounting.service.AccountService;
import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.accounting.service.SaleOrderService;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class SalesOrderTest {

    @Autowired
    private SaleOrderController saleOrderController;

    @MockBean
    private SaleOrderService saleOrderService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private LedgerService ledgerService;
    @MockBean
    private LogService logService;


    @Test
    public void getAllSaleOrdersTest()
    {
        SaleOrder s1 = new SaleOrder();

        List<SaleOrder> saleOrders = new ArrayList<SaleOrder>();
        saleOrders.add(s1);

        doReturn(saleOrders).when(saleOrderService).getAllSaleOrders();

        ResponseEntity<?> result = saleOrderController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getOneSaleOrderTest()
    {
        int id = 1;
        SaleOrder s1 = new SaleOrder();
        Optional<SaleOrder> saleOrder = Optional.of(s1);

        doReturn(saleOrder).when(saleOrderService).getSaleOrder(id);

        ResponseEntity<?> result = saleOrderController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1;

        doReturn(Optional.empty()).when(saleOrderService).getSaleOrder(id);

        ResponseEntity<?> result = saleOrderController.one(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void createSaleOrderTest()
    {
        SaleOrder s1 = new SaleOrder();

        doReturn(true).when(saleOrderService).addSaleOrder(s1);

        ResponseEntityWrapper result = saleOrderController.createSaleOrder(s1);

        assertEquals(HttpStatus.OK, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void createSaleOrderNotModifiedTest()
    {
        SaleOrder s1 = new SaleOrder();

        doReturn(false).when(saleOrderService).addSaleOrder(s1);

        ResponseEntityWrapper result = saleOrderController.createSaleOrder(s1);

        assertEquals(HttpStatus.NOT_MODIFIED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void receivePaymentTest()
    {
        int id = 1;
        SaleOrder s1 = new SaleOrder();
        Optional<SaleOrder> saleOrder = Optional.of(s1);

        doReturn(saleOrder).when(saleOrderService).getSaleOrder(id);

        ResponseEntityWrapper result = saleOrderController.receivePayment(id);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void receivePaymentBadRequestTest()
    {
        int id = 1;

        doReturn(Optional.empty()).when(saleOrderService).getSaleOrder(id);

        ResponseEntityWrapper result = saleOrderController.receivePayment(id);

        assertEquals(HttpStatus.BAD_REQUEST, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void shipBikeTest()
    {
        int id = 1;
        SaleOrder s1 = new SaleOrder();
        Optional<SaleOrder> saleOrder = Optional.of(s1);

        doReturn(saleOrder).when(saleOrderService).getSaleOrder(id);

        ResponseEntityWrapper result = saleOrderController.shipBike(id);

        assertEquals(HttpStatus.OK, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void shipBikeBadRequestTest()
    {
        int id = 1;

        doReturn(Optional.empty()).when(saleOrderService).getSaleOrder(id);

        ResponseEntityWrapper result = saleOrderController.shipBike(id);

        assertEquals(HttpStatus.BAD_REQUEST, result.getResponseEntity().getStatusCode());
    }
}



