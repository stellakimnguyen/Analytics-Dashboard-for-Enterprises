package com.soen390.erp.accounting;

import com.soen390.erp.accounting.controller.ClientController;
import com.soen390.erp.accounting.exceptions.ClientNotFoundException;
import com.soen390.erp.accounting.exceptions.InvalidClientException;
import com.soen390.erp.accounting.model.Client;
import com.soen390.erp.accounting.service.ClientService;
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
public class ClientTest {

    @Autowired
    private ClientController clientController;

    @MockBean
    ClientService clientService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private LogService logService;

    @Test
    public void getAllClientsTest()
    {
        Client c1 = new Client();

        List<Client> clients = new ArrayList<>();
        clients.add(c1);

        when(clientService.findAllClients()).thenReturn(clients);

        List<Client> result = clientController.getAllClients();

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findClientByIdTest()
    {
        int id = 1;
        Client s1 = new Client();
        doReturn(s1).when(clientService).findClientById(id);

        ResponseEntity<?> result = clientController.findClientById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void findClientByIdForbiddenTest()
    {
        int id = 1;
        String errorMessage = "Could not find client " + id;

        doThrow(new ClientNotFoundException(id)).when(clientService).findClientById(id);

        ResponseEntity<?> result = clientController.findClientById(id);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

        assertEquals(errorMessage, result.getBody());
    }

    @Test
    public void addClientTest()
    {
        Client s1 = new Client();
        doReturn(s1).when(clientService).saveClient(s1);

        ResponseEntity<?> result = clientController.addClient(s1);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    public void addClientForbiddenTest()
    {
        Client s1 = new Client();

        doThrow(new InvalidClientException()).when(clientService).saveClient(s1);

        ResponseEntity<?> result = clientController.addClient(s1);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void deleteClientByIdTest()
    {
        int id = 1;
        doReturn(true).when(clientService).deleteClientById(id);

        ResponseEntity<?> result = clientController.deleteClientById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteClientByIdNotFoundTest()
    {
        int id = 1;
        doReturn(false).when(clientService).deleteClientById(id);

        ResponseEntity<?> result = clientController.deleteClientById(id);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
