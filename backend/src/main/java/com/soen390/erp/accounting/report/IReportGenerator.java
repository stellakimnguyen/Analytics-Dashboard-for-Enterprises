package com.soen390.erp.accounting.report;

import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.accounting.service.PurchaseOrderService;
import com.soen390.erp.accounting.service.SaleOrderService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface IReportGenerator {

    void generateLedgerReport(LedgerService ledgerService) throws IOException;
    void generateSaleOrderReport(SaleOrderService saleOrderService) throws IOException;
    void generatePurchaseOrderReport(PurchaseOrderService purchaseOrderService) throws IOException;
    ByteArrayInputStream getInputStream();
    void setResponse(HttpServletResponse response);
}
