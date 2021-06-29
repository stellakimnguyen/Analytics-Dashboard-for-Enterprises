package com.soen390.erp.accounting.report;

import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.model.PurchaseOrder;
import com.soen390.erp.accounting.model.SaleOrder;
import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.accounting.service.PurchaseOrderService;
import com.soen390.erp.accounting.service.SaleOrderService;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class CsvReportGenerator implements IReportGenerator {

    private HttpServletResponse response;

    @Override
    public void generateLedgerReport(LedgerService ledgerService) throws IOException
    {
        List<Ledger> ledgers = ledgerService.findAllLedgers();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Ledger ID", "Amount", "Date", "Debit Amount ID",
                "Credit Account Id", "Purchase Oderer Id", "Sale Order Id"};

        String[] nameMapping = {"id", "amount", "date", "debitAccount",
                "creditAccount", "purchaseOrder","saleOrder" };

        csvWriter.writeHeader(csvHeader);

        for (Ledger ledger : ledgers)
        {
            csvWriter.write(ledger, nameMapping);
        }

        csvWriter.close();
    }

    @Override
    public void generateSaleOrderReport(SaleOrderService saleOrderService)
            throws IOException
    {
        List<SaleOrder> listSales = saleOrderService.getAllSaleOrders();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(
                response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Sale Oder ID",  "Plant", "date", "Client",
                "Total Amount", "Discount", "Discount Amount", "Tax",
                "Tax Amount", "Total", "Paid", "Shipped" , "Sale Items"};

        String[] nameMapping = {"id", "plant", "date", "client",  "totalAmount",
                "discount","discountAmount", "tax", "taxAmount", "grandTotal",
                "paid", "shipped", "saleOrderItems" };

        csvWriter.writeHeader(csvHeader);

        for (SaleOrder sale : listSales)
        {
            csvWriter.write(sale, nameMapping);
        }

        csvWriter.close();
    }

    @Override
    public void generatePurchaseOrderReport(PurchaseOrderService purchaseOrderService)
            throws IOException
    {
        List<PurchaseOrder> purchaseOrders =
                purchaseOrderService.getAllPurchaseOrders();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"Purchase Oder ID", "Plant", "date", "Supplier",
                "Total Amount", "discount", "Discount Amount", "Tax",
                "Tax Amount", "Paid", "Received" , "Purchased Items"};

        String[] nameMapping = {"id", "plant", "date", "supplier", "totalAmount",
                "discount","discountAmount", "tax", "taxAmount", "grandTotal",
                "paid", "received", "purchaseOrderItems" };

        csvWriter.writeHeader(csvHeader);

        for (PurchaseOrder purchase : purchaseOrders)
        {
            csvWriter.write(purchase, nameMapping);
        }

        csvWriter.close();
    }

    public void setResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    @Override
    public ByteArrayInputStream getInputStream()
    {
        return null;
    }
}
