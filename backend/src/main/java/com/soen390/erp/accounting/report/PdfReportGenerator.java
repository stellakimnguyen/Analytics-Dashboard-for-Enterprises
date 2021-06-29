package com.soen390.erp.accounting.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.model.PurchaseOrder;
import com.soen390.erp.accounting.model.SaleOrder;
import com.soen390.erp.accounting.service.LedgerService;
import com.soen390.erp.accounting.service.PurchaseOrderService;
import com.soen390.erp.accounting.service.SaleOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public class PdfReportGenerator implements IReportGenerator {

    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    private Font header1Font = new Font(Font.FontFamily.UNDEFINED, 16,
            Font.BOLD);

    private Font tableTitleFont = new Font(Font.FontFamily.UNDEFINED, 11,
            Font.BOLD);

    private Font regularFont = new Font(Font.FontFamily.UNDEFINED, 11,
            Font.NORMAL);

    private final Logger logger =
            LoggerFactory.getLogger(PdfReportGenerator.class);

    public PdfReportGenerator()
    {
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public void generateLedgerReport(LedgerService ledgerService)
    {
        List<Ledger> ledgers = ledgerService.findAllLedgers();
        Document document = new Document();
        Paragraph preface = new Paragraph();

        try
        {
            preface.add(new Paragraph("Ledger Report", header1Font));

            addEmptyLine(preface, 1);

            preface.add(new Paragraph(
                    "Report generated on : " + new Date() ));

            addEmptyLine(preface, 2);

            PdfPTable table = new PdfPTable(7);

            table.setWidthPercentage(90);
            table.setWidths(new int[]{1, 1, 2, 1, 1, 1, 1});

            addPdfCell(table, "id", tableTitleFont);

            addPdfCell(table, "Amount", tableTitleFont);

            addPdfCell(table, "Date", tableTitleFont);

            addPdfCell(table, "Credit Account", tableTitleFont);

            addPdfCell(table, "Debit Account", tableTitleFont);

            addPdfCell(table, "Purchase Order", tableTitleFont);

            addPdfCell(table, "Sale Order", tableTitleFont);

            for (Ledger ledger : ledgers)
            {
                addPdfCell(table, Integer.toString(ledger.getId()), regularFont);

                addPdfCell(table, Double.toString(ledger.getAmount()), regularFont);

                addPdfCell(table, String.valueOf(ledger.getDate()), regularFont);

                addPdfCell(table, Integer.toString(ledger.getCreditAccount()
                        .getId()), regularFont);

                addPdfCell(table, Integer.toString(ledger.getDebitAccount()
                        .getId()), regularFont);

                String purchaseOrderId = "--";

                if (ledger.getPurchaseOrder() != null)
                {
                    purchaseOrderId =
                            Integer.toString(ledger.getPurchaseOrder().getId());
                }

                addPdfCell(table, purchaseOrderId, regularFont);

                String saleOrderId = "--";

                if (ledger.getSaleOrder() != null)
                {
                    saleOrderId =
                            Integer.toString(ledger.getSaleOrder().getId());
                }

                addPdfCell(table,saleOrderId, regularFont);
            }

            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(preface);
            document.add(table);

            document.close();

        } catch (DocumentException ex)
        {
            logger.error("Error occurred Generating the Ledger PDF", ex);
        }

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public void generateSaleOrderReport(SaleOrderService saleOrderService)
    {
        List<SaleOrder> saleOrders = saleOrderService.getAllSaleOrders();
        Document document = new Document();
        Paragraph preface = new Paragraph();

        try
        {
            preface.add(new Paragraph("Sale Orders Report", header1Font));

            addEmptyLine(preface, 1);

            preface.add(new Paragraph(
                    "Report generated on : " + new Date() ));

            addEmptyLine(preface, 2);

            PdfPTable table = new PdfPTable(12);

            table.setWidthPercentage(99);
            table.setWidths(new int[]{1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

            addPdfCell(table, "id", tableTitleFont);

            addPdfCell(table, "Date", tableTitleFont);

            addPdfCell(table, "Discount", tableTitleFont);

            addPdfCell(table, "Discount \nAmount", tableTitleFont);

            addPdfCell(table, "Grand \nTotal", tableTitleFont);

            addPdfCell(table, "Paid", tableTitleFont);

            addPdfCell(table, "Shipped", tableTitleFont);

            addPdfCell(table, "Tax", tableTitleFont);

            addPdfCell(table, "Tax Amount", tableTitleFont);

            addPdfCell(table, "Total Amount", tableTitleFont);

            addPdfCell(table, "Client", tableTitleFont);

            addPdfCell(table, "Plant", tableTitleFont);

            for (SaleOrder saleOrder : saleOrders)
            {
                addPdfCell(table, Integer.toString(saleOrder.getId()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.getDate()), regularFont);

                addPdfCell(table, Double.toString(saleOrder.getDiscount()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.getDiscountAmount()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.getGrandTotal()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.isPaid()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.isShipped()), regularFont);

                addPdfCell(table,String.valueOf(saleOrder.getTax()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.getTaxAmount()), regularFont);

                addPdfCell(table, String.valueOf(saleOrder.getTotalAmount()), regularFont);

                addPdfCell(table, Integer.toString(saleOrder.getClient().getId())
                        + ", " + saleOrder.getClient().getName(), regularFont);

                addPdfCell(table, Integer.toString(saleOrder.getPlant().getId())
                        + ", " + saleOrder.getPlant().getName(), regularFont);
            }

            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(preface);
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred Generating the Sale Order PDF", ex);
        }

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public void generatePurchaseOrderReport(PurchaseOrderService purchaseOrderService)
    {
        List<PurchaseOrder> purchaseOrders =
                purchaseOrderService.getAllPurchaseOrders();
        Document document = new Document();
        Paragraph preface = new Paragraph();

        try
        {
            preface.add(new Paragraph("Purchase Orders Report", header1Font));

            addEmptyLine(preface, 1);

            preface.add(new Paragraph(
                    "Report generated on : " + new Date() ));

            addEmptyLine(preface, 2);

            PdfPTable table = new PdfPTable(12);

            table.setWidthPercentage(98);
            table.setWidths(new int[]{1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

            addPdfCell(table, "id", tableTitleFont);

            addPdfCell(table, "Date", tableTitleFont);

            addPdfCell(table, "Discount", tableTitleFont);

            addPdfCell(table, "Discount Amount", tableTitleFont);

            addPdfCell(table, "Grand Total", tableTitleFont);

            addPdfCell(table, "Tax", tableTitleFont);

            addPdfCell(table, "Tax Amount", tableTitleFont);

            addPdfCell(table, "Total Amount", tableTitleFont);

            addPdfCell(table, "Plant", tableTitleFont);

            addPdfCell(table, "Supplier", tableTitleFont);

            addPdfCell(table, "Paid", tableTitleFont);

            addPdfCell(table, "Received", tableTitleFont);

            for (PurchaseOrder purchaseOrder : purchaseOrders) {

                PdfPCell cell;

                addPdfCell(table, Integer.toString(purchaseOrder.getId()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getDate()), regularFont);

                addPdfCell(table, Double.toString(purchaseOrder.getDiscount()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getDiscountAmount()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getGrandTotal()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getTax()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getTaxAmount()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.getTotalAmount()), regularFont);

                addPdfCell(table, Integer.toString(purchaseOrder.getPlant().getId())
                        + ", " + purchaseOrder.getPlant().getName(), regularFont);

                addPdfCell(table, Integer.toString(purchaseOrder.getSupplier().getId())
                        + ", " + purchaseOrder.getSupplier().getName(), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.isPaid()), regularFont);

                addPdfCell(table, String.valueOf(purchaseOrder.isReceived()), regularFont);

            }

            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(preface);
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred Generating the Purchase Order PDF",
                    ex);
        }

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ByteArrayInputStream getInputStream()
    {
        return inputStream;
    }

    private void addEmptyLine(Paragraph paragraph, int number)
    {
        for (int i = 0; i < number; i++)
        {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addPdfCell(PdfPTable table, String phrase,
                            Font font)
    {
        PdfPCell cell = new PdfPCell(new Phrase(phrase, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    @Override
    public void setResponse(HttpServletResponse response) {}
}
