package com.soen390.erp.accounting.controller;

import com.soen390.erp.accounting.exceptions.LedgerNotFoundException;
import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.report.CsvReportGenerator;
import com.soen390.erp.accounting.report.IReportGenerator;
import com.soen390.erp.accounting.report.PdfReportGenerator;
import com.soen390.erp.accounting.repository.LedgerRepository;
import com.soen390.erp.accounting.service.LedgerModelAssembler;
import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class LedgerController {

    private final LedgerRepository ledgerRepository;
    private final LedgerModelAssembler assembler;
    private final LedgerService ledgerService;
    private final EmailService emailService;
    private final LogService logService;
    private static final String category = "accounting";

    public LedgerController(LedgerRepository ledgerRepository,
                LedgerModelAssembler assembler,
                LedgerService ledgerService,
                EmailService emailService,
                LogService logService)

    {
        this.ledgerRepository=ledgerRepository;
        this.assembler=assembler;
        this.ledgerService = ledgerService;
        this.emailService = emailService;
        this.logService = logService;
    }

    @GetMapping("/ledger")
    public ResponseEntity<?> all()
    {
        List<EntityModel<Ledger>> ledger = ledgerService.assembleToModel();
        logService.addLog("Retrieved all ledgers.", category);
        return ResponseEntity.ok().body(
                CollectionModel.of(ledger, linkTo(methodOn(
                        LedgerController.class).all()).withSelfRel()));
    }

    @GetMapping(path = "/ledger/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {

        Ledger ledger = ledgerRepository.findById(id)
                .orElseThrow(() -> new LedgerNotFoundException(id));
        logService.addLog("Retrieved ledger with id "+id+".", category);
        return ResponseEntity.ok().body(assembler.toModel(ledger));
    }

    @PostMapping("/ledger")
    public ResponseEntityWrapper newTransaction(@RequestBody Ledger ledger){

        // Todo add account transaction / add inventory updates
        EntityModel<Ledger> entityModel = assembler.toModel(
                ledgerRepository.save(ledger));

        String message = "A new ledger has been created with id " + ledger.getId();
        EmailToSend email = EmailToSend.builder().to("accountant@msn.com").subject("Created Ledger").body(message).build();
        emailService.sendMail(email);
        logService.addLog(message, category);

        return new ResponseEntityWrapper(ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
                , "The ledger was successfully created with id " + ledger.getId());

    }

    @ResponseBody
    @ExceptionHandler(LedgerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String partNotFoundException(LedgerNotFoundException ex){
        return ex.getMessage();
    }

    @GetMapping(value = "/ledger/report/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf() throws IOException
    {
        var headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=ledgersReport" +
                        ".pdf");

        IReportGenerator pdfReportGenerator = new PdfReportGenerator();
        ledgerService.accept(pdfReportGenerator);
        ByteArrayInputStream inputStream = pdfReportGenerator.getInputStream();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/ledger/report/csv")
    public void exportToCSV(HttpServletResponse response)
            throws IOException
    {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue =
                "attachment; filename=ledgerReport_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        IReportGenerator csvReportGenerator = new CsvReportGenerator();
        csvReportGenerator.setResponse(response);
        ledgerService.accept(csvReportGenerator);
    }
}