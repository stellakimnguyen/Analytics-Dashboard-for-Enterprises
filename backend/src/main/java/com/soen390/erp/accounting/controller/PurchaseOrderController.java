package com.soen390.erp.accounting.controller;

import com.soen390.erp.accounting.exceptions.PurchaseNotFoundException;
import com.soen390.erp.accounting.model.PurchaseOrder;
import com.soen390.erp.accounting.report.CsvReportGenerator;
import com.soen390.erp.accounting.report.IReportGenerator;
import com.soen390.erp.accounting.report.PdfReportGenerator;
import com.soen390.erp.accounting.service.PurchaseOrderService;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
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
import java.util.Optional;

@AllArgsConstructor
@RestController
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final LogService logService;
    private static final String category = "accounting";

    @GetMapping(path = "/PurchaseOrders")
    public ResponseEntity<?> all(){

        logService.addLog("Retrieved all purchase orders.", category);
        //TODO: use stream and return a mapped collection or use assembler
        return ResponseEntity.ok().body(purchaseOrderService.getAllPurchaseOrders());
    }

    @GetMapping(path = "/PurchaseOrders/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {

        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(id)
                .orElseThrow(() -> new PurchaseNotFoundException("No order with id " + id));

        logService.addLog("Retrieved purchase order with id "+id+".", category);
        return ResponseEntity.ok().body(purchaseOrder);

    }

    @PostMapping(path = "/PurchaseOrders")
    public ResponseEntityWrapper createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        //TODO: validate input
        //TODO: validate po items has quantity > 0

        purchaseOrder.setPaid(false);
        purchaseOrder.setReceived(false);

        boolean isSuccessful = purchaseOrderService.addPurchaseOrder(purchaseOrder);
        if (isSuccessful == true){
            logService.addLog("Created purchase order.", category);
            //TODO: debug if id has value
            return new ResponseEntityWrapper(ResponseEntity.ok().body(purchaseOrder.getId()), "The Purchase Order has been created with id " + purchaseOrder.getId());
        }else{
            return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null), "Purchase order could not be created.");
        }
    }

    @PutMapping(path = "/PurchaseOrders/{id}/MakePayment")
    public ResponseEntityWrapper makePayment(@PathVariable int id){

        //region validation
        //check if purchase order exists
        Optional<PurchaseOrder> purchaseOrderOptional = purchaseOrderService.getPurchaseOrder(id);
        if (purchaseOrderOptional.isEmpty()){
            logService.addLog("Failed to make payment for purchase order with id "+id+". No such purchase order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Could not find Purchase Order with id: " + id + ".");
        }
        PurchaseOrder purchaseOrder = purchaseOrderOptional.get();
        //check if transaction valid
        if(purchaseOrder.isPaid()){
            logService.addLog("Failed to make payment for purchase order with id "+id+". Already paid for.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Purchase Order has already been paid.");
        }
        //TODO check if bank balance is more than grand total
        //endregion

        purchaseOrderService.makePaymentTransactions(purchaseOrder);
        logService.addLog("Paid for purchase order with id "+id+".", category);
        //region return
        return new ResponseEntityWrapper(ResponseEntity.ok().build(), "Purchase Order with id " + id + " has been paid.");
        //endregion
    }
    @PutMapping(path = "/PurchaseOrders/{id}/ReceiveMaterial")
    public ResponseEntityWrapper receiveMaterial(@PathVariable int id){
        //region validation
        //check if purchase order exists
        Optional<PurchaseOrder> purchaseOrderOptional = purchaseOrderService.getPurchaseOrder(id);
        if (purchaseOrderOptional.isEmpty()){
            logService.addLog("Failed to receive materials for purchase order with id "+id+". No such purchase order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Could not find Purchase Order with id: " + id + ".");
        }
        PurchaseOrder purchaseOrder = purchaseOrderOptional.get();
        //check if transaction valid
        if(purchaseOrder.isReceived()){

            logService.addLog("Failed to receive material for purchase order with id "+id+". Already received.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Purchase Order has already been received.");
        }
        //TODO check if inventory balance is more than grand total
        //endregion

        purchaseOrderService.receiveMaterialTransactions(purchaseOrder);
        logService.addLog("Successfully received material for purchase order with id "+id+".", category);
        //region return

        return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).build(), "Receiving materials from Purchase Order with id" + id + ".");
        //endregion
    }

    @GetMapping(value = "/PurchaseOrders/report/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf() throws IOException
    {
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " +
                "filename=purchaseOrderReport" +
                ".pdf");

        IReportGenerator pdfReportGenerator = new PdfReportGenerator();
        purchaseOrderService.accept(pdfReportGenerator);
        ByteArrayInputStream inputStream = pdfReportGenerator.getInputStream();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/PurchaseOrders/report/csv")
    public void exportToCSV(HttpServletResponse response)
            throws IOException
    {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue =
                "attachment; filename=purchaseOrdersReport_" + currentDateTime +
                        ".csv";
        response.setHeader(headerKey, headerValue);

        IReportGenerator csvReportGenerator = new CsvReportGenerator();
        csvReportGenerator.setResponse(response);
        purchaseOrderService.accept(csvReportGenerator);
    }
}