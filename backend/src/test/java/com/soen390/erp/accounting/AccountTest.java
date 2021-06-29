package com.soen390.erp.accounting;

import com.soen390.erp.accounting.controller.AccountController;
import com.soen390.erp.accounting.exceptions.AccountNotFoundException;
import com.soen390.erp.accounting.model.Account;
import com.soen390.erp.accounting.repository.AccountRepository;
import com.soen390.erp.accounting.service.AccountModelAssembler;
import com.soen390.erp.accounting.service.AccountService;
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
public class AccountTest {
    @Autowired
    AccountController accountController;

    @MockBean
    private AccountService accountService;
    @MockBean
    private AccountModelAssembler assembler;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private EmailService emailService;
    @MockBean
    private LogService logService;


    @Test
    public void allTest()
    {
        List<EntityModel<Account>> accounts = new ArrayList<>();

        doReturn(accounts).when(accountService).assembleToModel();

        ResponseEntity<?> result = accountController.all();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneTest()
    {
        int id = 1 ;
        Account a1 = new Account();

        doReturn(Optional.of(a1)).when(accountRepository).findById(id);

        ResponseEntity<?> result = accountController.one(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void oneNotFoundTest()
    {
        int id = 1 ;

        doThrow(new AccountNotFoundException(id)).when(accountRepository)
                .findById(id);

        AccountNotFoundException result =
                Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountController.one(id);
        });

        assertEquals("Could not find the account " + id, result
                .getMessage());
    }

    @Test
    public void newTransactionTest()
    {
        int id = 1 ;
        Account a1 = mock(Account.class);
        EntityModel<Account> entityModel =
                (EntityModel<Account>)mock(EntityModel.class);
        Link link = spy(Link.class);

        doReturn(a1).when(accountRepository).save(a1);
        doReturn(entityModel).when(assembler).toModel(a1);

        doReturn(a1).when(entityModel).getContent();
        doReturn(id).when(a1).getId();

        doReturn(link).when(entityModel).getRequiredLink(IanaLinkRelations.SELF);
        doReturn(null).when(link).toUri();

        ResponseEntityWrapper result = accountController.newTransaction(a1);

        assertEquals(HttpStatus.CREATED, result.getResponseEntity().getStatusCode());
    }

    @Test
    public void partNotFoundExceptionTest()
    {
        int id = 1 ;

        String result =
                accountController.partNotFoundException(
                        new AccountNotFoundException(id));

        assertEquals("Could not find the account " + id, result);
    }
}
