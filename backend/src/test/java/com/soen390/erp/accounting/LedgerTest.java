package com.soen390.erp.accounting;

import com.soen390.erp.accounting.controller.LedgerController;
import com.soen390.erp.accounting.exceptions.LedgerNotFoundException;
import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.repository.LedgerRepository;
import com.soen390.erp.accounting.service.LedgerModelAssembler;
import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.service.EmailService;
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
public class LedgerTest {
    @Autowired
    LedgerController ledgerController;

    @MockBean
    private LedgerService ledgerService;
    @MockBean
    private LedgerModelAssembler assembler;
    @MockBean
    private LedgerRepository ledgerRepository;
    @MockBean
    private EmailService emailService;
    @MockBean
    private LogService logService;


    @Test
    public void allTest()
    {
        List<EntityModel<Ledger>> ledgers = new ArrayList<>();

        doReturn(ledgers).when(ledgerService).assembleToModel();

        ResponseEntity<?> result = ledgerController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Ledger l1 = new Ledger();

        doReturn(Optional.of(l1)).when(ledgerRepository).findById(id);

        ResponseEntity<?> result = ledgerController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new LedgerNotFoundException(id)).when(ledgerRepository)
                .findById(id);

        LedgerNotFoundException result =
                Assertions.assertThrows(LedgerNotFoundException.class, () -> {
                    ledgerController.one(id);
                });

        assertEquals("Could not find the ledger " + id, result
                .getMessage());
    }

    @Test
    public void newTransactionTest()
    {
        Ledger l1 = new Ledger();
        EntityModel<Ledger> entityModel =
                (EntityModel<Ledger>)mock(EntityModel.class);
        Link link = spy(Link.class);

        doReturn(l1).when(ledgerRepository).save(l1);
        doReturn(entityModel).when(assembler).toModel(l1);

        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = ledgerController.newTransaction(l1);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void partNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                ledgerController.partNotFoundException(
                        new LedgerNotFoundException(id));

        assertEquals("Could not find the ledger " + id, result);
    }
}
