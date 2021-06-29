package com.soen390.erp.inventory;

import com.soen390.erp.inventory.controller.ClientOrderController;
import com.soen390.erp.inventory.exceptions.InvalidClientOrderException;
import com.soen390.erp.inventory.model.ClientOrder;
import com.soen390.erp.inventory.service.ClientOrderService;
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
public class ClientOrderTest {

    @Autowired
    private ClientOrderController clientOrderController;

    @MockBean
    private ClientOrderService clientOrderService;

    @Test
    public void getAllClientOrdersTest()
    {
        ClientOrder s1 = new ClientOrder();
        ClientOrder s2 = new ClientOrder();

        List<ClientOrder> clientOrders = new ArrayList<>();
        clientOrders.add(s1);
        clientOrders.add(s2);

        doReturn(clientOrders).when(clientOrderService).findAllClientOrders();

        List<ClientOrder> result = clientOrderController.getAllClientOrders();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void addClientOrderTest()
    {
        ClientOrder s1 = mock(ClientOrder.class);

        doReturn(s1).when(clientOrderService).addClientOrder(s1);

        ResponseEntity<?> result = clientOrderController.addClientOrder(s1);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void addClientOrderForbiddenTest()
    {
        ClientOrder s1 = mock(ClientOrder.class);

        doThrow(new InvalidClientOrderException()).when(clientOrderService)
                .addClientOrder(s1);

        ResponseEntity<?> result = clientOrderController.addClientOrder(s1);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }
}
