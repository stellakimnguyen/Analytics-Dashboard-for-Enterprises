package com.soen390.erp.accounting.controller;

import com.soen390.erp.accounting.model.SaleOrder;
import com.soen390.erp.accounting.report.CsvReportGenerator;
import com.soen390.erp.accounting.report.IReportGenerator;
import com.soen390.erp.accounting.report.PdfReportGenerator;
import com.soen390.erp.accounting.service.SaleOrderService;
import com.soen390.erp.configuration.model.BooleanWrapper;
import com.soen390.erp.configuration.model.ResponseEntityWrapper;
import com.soen390.erp.configuration.service.LogService;
import com.soen390.erp.inventory.exceptions.NotEnoughMaterialInPlantException;
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
public class SaleOrderController {
    private final SaleOrderService saleOrderService;
    private final LogService logService;
    private static final String category = "accounting";


    @GetMapping(path = "/SaleOrders")
    public ResponseEntity<?> all(){
        //TODO: use stream and return a mapped collection or use assembler
        return ResponseEntity.ok().body(saleOrderService.getAllSaleOrders());
    }

    @GetMapping(path = "/SaleOrders/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {

        Optional<SaleOrder> saleOrder = saleOrderService.getSaleOrder(id);

        if (saleOrder.isPresent()){
            logService.addLog("Retrieved sale order with id "+id+".", category);
            return ResponseEntity.ok().body(saleOrder.get());
        }else{
            logService.addLog("Failed to retrieved client with id "+id+". Doesn't exist.", category);
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(path = "/SaleOrders")
    public ResponseEntityWrapper createSaleOrder(@RequestBody SaleOrder saleOrder){
        //TODO: validate input

        saleOrder.setPaid(false);
        saleOrder.setShipped(false);

        boolean isSuccessful = saleOrderService.addSaleOrder(saleOrder);
        if (isSuccessful == true){
            //TODO: debug if id has value
            String message = "Created Sale Order with id " + saleOrder.getId() + ".";
            logService.addLog(message, category);
            return new ResponseEntityWrapper(ResponseEntity.ok().body(saleOrder.getId()), message);
        }else{
            logService.addLog("Failed to create sale order with id "+saleOrder.getId()+".", category);
            return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null), "Unable to created new Sale Order.");
        }
    }

    @PutMapping(path = "/SaleOrders/{id}/ReceivePayment")
    public ResponseEntityWrapper receivePayment(@PathVariable int id){

        //region validation
        //check if sale order exists
        Optional<SaleOrder> saleOrderOptional = saleOrderService.getSaleOrder(id);
        if (saleOrderOptional.isEmpty()){
            logService.addLog("Failed to receive payment for sale order with id "+id+". No such sale order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale Order with id " + id + " could not be found.");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        //check if transaction valid
        if(saleOrder.isPaid()){
            logService.addLog("Failed to receive payment for sale order with id "+id+". Already paid for.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale Order with id " + id + " has already been paid.");
        }
        //TODO check if bank balance is more than grand total
        //TODO check if new status is valid
        //endregion

        saleOrderService.receivePaymentTransactions(saleOrder);
        logService.addLog("Received payment for sale order with id "+id+".", category);

        //region return
        return new ResponseEntityWrapper(ResponseEntity.status(HttpStatus.CREATED).build(), "Sale Order with id " + id + " is now paid.");
        //endregion
    }

    @PutMapping(path = "/SaleOrders/{id}/ShipBike")
    public ResponseEntityWrapper shipBike(@PathVariable int id){

        //region validation
        //check if sale order exists
        Optional<SaleOrder> saleOrderOptional = saleOrderService.getSaleOrder(id);
        if (saleOrderOptional.isEmpty()){
            logService.addLog("Failed to ship bike for sale order with id "+id+". No such sale order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Unable to find Sale Order with id " + id + ".");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        //check if transaction valid
        if(saleOrder.isShipped()){
            logService.addLog("Failed to ship bike for sale order with id "+id+". Already shipped.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale Order with id " + id + " has already been shipped.");
        }

        //TODO check if bank balance is more than grand total
        //TODO check if new status is valid
        //TODO check if bike exists in plant and enough quantity
        //endregion

        saleOrderService.shipBikeTransactions(saleOrder);
        logService.addLog("Shipped bike for sale order with id "+id+".", category);
        //region return
        return new ResponseEntityWrapper(ResponseEntity.ok().build(), "Sale order with id " + id + " is now shipped.");
        //endregion
    }

    @PostMapping(path = "/SaleOrders/{id}/MakeBike")
    public ResponseEntityWrapper makeBike(@PathVariable int id){

        //region validation
        //check if sale order exists
        Optional<SaleOrder> saleOrderOptional = saleOrderService.getSaleOrder(id);
        if (saleOrderOptional.isEmpty()){
            logService.addLog("Failed to make bike for sale order with id "+id+". No such sale order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Unable to find Sale Order with id " + id + ".");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        //we assume we wouldn't get any make requests unless it is valid because it id preceded by a check step
        //check if transaction valid
        if(saleOrder.isShipped()){
            logService.addLog("Failed to make bike for sale order with id "+id+". Already shipped.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale Order with id " + id + " has already been made.");
        }
        //endregion

        //deduct parts from plant and add bike to plant
        try {
            saleOrderService.makePlantBike(saleOrder);
        }
        catch (NotEnoughMaterialInPlantException e){
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale order with id " + id + " could not make the bikes.");
        }
        logService.addLog("Made bike for sale order with id "+id+".", category);
        //region return
        return new ResponseEntityWrapper(ResponseEntity.ok().build(), "Sale order with id " + id + " has all its bikes made.");
        //endregion

    }

    @PostMapping(path = "/SaleOrders/{id}/GatherBikeParts")
    public ResponseEntityWrapper gatherBikeParts(@PathVariable int id){

        //region validation
        //check if sale order exists
        Optional<SaleOrder> saleOrderOptional = saleOrderService.getSaleOrder(id);
        if (saleOrderOptional.isEmpty()){
            logService.addLog("Failed to make bike for sale order with id "+id+". No such sale order exists.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Unable to find Sale Order with id " + id + ".");
        }
        SaleOrder saleOrder = saleOrderOptional.get();
        //we assume we wouldn't get any make requests unless it is valid because it id preceded by a check step
        //check if transaction valid
        if(saleOrder.isShipped()){
            logService.addLog("Sale order with id "+id+". Already shipped.", category);
            return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), "Sale Order with id " + id + " has already been shipped.");
        }
        //endregion

        //deduct parts from plant and add bike to plant
        BooleanWrapper result = saleOrderService.gatherBikeParts(saleOrder);
        if(result.isResult()) {
            logService.addLog("Gather bike for sale order with id " + id + ".", category);
            //region return
            return new ResponseEntityWrapper(ResponseEntity.ok().build(), "Sale order with id " + id + " has all its parts to make the bikes.");
        }
        return new ResponseEntityWrapper(ResponseEntity.badRequest().build(), result.getMessage());
        //endregion
    }

    @GetMapping(value = "/SaleOrders/report/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf() throws IOException
    {
        var headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=saleOrderReport" +
                        ".pdf");

        IReportGenerator pdfReportGenerator = new PdfReportGenerator();
        saleOrderService.accept(pdfReportGenerator);
        ByteArrayInputStream inputStream = pdfReportGenerator.getInputStream();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/SaleOrders/report/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException
    {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue =
                "attachment; filename=saleOrdersReport_" + currentDateTime +
                ".csv";
        response.setHeader(headerKey, headerValue);

        IReportGenerator csvReportGenerator = new CsvReportGenerator();
        csvReportGenerator.setResponse(response);
        saleOrderService.accept(csvReportGenerator);
    }

}