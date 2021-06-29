package com.soen390.erp.accounting.service;

import com.soen390.erp.accounting.model.Account;
import com.soen390.erp.accounting.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountModelAssembler assembler;

    public Optional<Account> getAccount(int id){
        Optional<Account> account = accountRepository.findById(id);
        return account;
    }

    public List<EntityModel<Account>> assembleToModel()
    {
        return accountRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}
