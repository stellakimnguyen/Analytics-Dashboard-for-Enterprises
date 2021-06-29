package com.soen390.erp.accounting.controller;

import com.soen390.erp.accounting.exceptions.AccountNotFoundException;
import com.soen390.erp.accounting.model.Account;
import com.soen390.erp.accounting.repository.AccountRepository;
import com.soen390.erp.accounting.service.AccountModelAssembler;
import com.soen390.erp.accounting.service.AccountService;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final AccountModelAssembler assembler;
    private final EmailService emailService;
    private final LogService logService;
    private static final String category = "accounting";

    public AccountController(AccountRepository accountRepository,
                             AccountModelAssembler assembler,
                             AccountService accountService,
                             EmailService emailService, LogService logService)
    {
        this.accountRepository = accountRepository;
        this.assembler = assembler;
        this.accountService = accountService;
        this.emailService = emailService;
        this.logService = logService;
    }
    {
        String category = "accounting";
    }

    @GetMapping("/account")
    public ResponseEntity<?> all() {

        logService.addLog("Retrieved all the accounts.", category);

        List<EntityModel<Account>> account = accountService.assembleToModel();

        return ResponseEntity.ok().body(
                CollectionModel.of(account, linkTo(methodOn(AccountController.class).all()).withSelfRel()));
    }

    @GetMapping(path = "/account/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        logService.addLog("Retrieved account with id "+id, category);

        return ResponseEntity.ok().body(assembler.toModel(account));
    }

    @PostMapping("/account")
    public ResponseEntityWrapper newTransaction(@RequestBody Account account){


        EntityModel<Account> entityModel = assembler.toModel(accountRepository.save(account));

        logService.addLog("Retrieved account with id "+entityModel.getContent().getId(), category);

        EmailToSend email = EmailToSend.builder().to("accountant@msn.com").subject("Created Account").body("A new account has been created with id " + account.getId()).build();
        emailService.sendMail(email);

        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return new ResponseEntityWrapper(ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
                , "The account was successfully created with id " + account.getId());
    }

    @ResponseBody
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String partNotFoundException(AccountNotFoundException ex){
        return ex.getMessage();
    }


}
