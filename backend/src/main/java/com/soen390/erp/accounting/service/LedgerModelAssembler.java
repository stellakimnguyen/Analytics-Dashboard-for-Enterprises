package com.soen390.erp.accounting.service;

import com.soen390.erp.accounting.controller.LedgerController;
import com.soen390.erp.accounting.model.Ledger;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LedgerModelAssembler implements RepresentationModelAssembler<Ledger, EntityModel<Ledger>> {

    @Override
    public EntityModel<Ledger> toModel(Ledger ledger) {
        return EntityModel.of(ledger,
                linkTo(methodOn(LedgerController.class).one(ledger.getId())).withSelfRel(),
                linkTo(methodOn(LedgerController.class).all()).withRel("ledger"));
    }
}
