package com.soen390.erp.accounting.service;

import com.soen390.erp.accounting.model.IReport;
import com.soen390.erp.accounting.model.Account;
import com.soen390.erp.accounting.model.Ledger;
import com.soen390.erp.accounting.model.SaleOrder;
import com.soen390.erp.accounting.model.SaleOrderItems;
import com.soen390.erp.accounting.report.IReportGenerator;
import com.soen390.erp.accounting.repository.SaleOrderRepository;
import com.soen390.erp.configuration.model.BooleanWrapper;
import com.soen390.erp.email.model.EmailToSend;
import com.soen390.erp.email.service.EmailService;
import com.soen390.erp.inventory.model.Plant;
import com.soen390.erp.inventory.repository.PlantRepository;
import com.soen390.erp.inventory.service.PlantService;
import com.soen390.erp.manufacturing.repository.BikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SaleOrderService implements IReport {
    private final SaleOrderRepository repository;
    private final PlantRepository plantRepository;
    private final BikeRepository bikeRepository;
    private final AccountService accountService;
    private final LedgerService ledgerService;
    private final EmailService emailService;
    private final PlantService plantService;

    public boolean addSaleOrder(SaleOrder saleOrder){
        // set the plant
        Plant plant = plantRepository.findById(1).get();
        saleOrder.setPlant(plant);

        // set date
        saleOrder.setDate(new Date());
        //calculate the total price
        double totalPrice = 0;
        for(SaleOrderItems saleOrderItem : saleOrder.getSaleOrderItems()){
            totalPrice += saleOrderItem.getQuantity()*saleOrderItem.getUnitPrice();
            bikeRepository.save(saleOrderItem.getBike());
        }
        saleOrder.setTotalAmount(totalPrice);

        //tax price
        double tax = .15;
        saleOrder.setTax(tax);
        double taxAmount = totalPrice * tax;
        saleOrder.setTaxAmount(taxAmount);

        //grand total
        double grandTotal = taxAmount+totalPrice;
        saleOrder.setGrandTotal(grandTotal);

        saleOrder = repository.save(saleOrder);
        if (saleOrder.getId() != 0){
            EmailToSend email = EmailToSend.builder().to("accountant@msn.com").subject("New Sale Order").body("A new Sale Order has been received with id " + saleOrder.getId()).build();
            emailService.sendMail(email);
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<SaleOrder> getAllSaleOrders()
    {
        return repository.findAll();
    }

    public Optional<SaleOrder>  getSaleOrder(int id) {
        Optional<SaleOrder> saleOrder = repository.findById(id);
        return saleOrder;
    }

    public void receivePaymentTransactions( SaleOrder saleOrder)
    {
        //get amount from po
        double amount = saleOrder.getGrandTotal();

        //region accounts
        //FIXME fetch bank and inventory accounts using enum and not id.
        int bankAccountId = 12; //wrong assumption
        int accountReceivableAccountId = 10; //wrong assumption

        Account bank = accountService.getAccount(bankAccountId).get();
        Account accountReceivable = accountService.getAccount(accountReceivableAccountId).get();

        bank.setBalance(bank.getBalance() + amount);
        accountReceivable.setBalance(accountReceivable.getBalance() - amount);
        //endregion

        //region sale order
        //update status
        saleOrder.setPaid(true);
        //endregion

        //region ledger
        //TODO insert a ledger entry
        Ledger ledgerEntry = new Ledger();
        ledgerEntry.setDebitAccount(bank);
        ledgerEntry.setCreditAccount(accountReceivable);
        ledgerEntry.setDate(new Date());
        ledgerEntry.setAmount(amount);
        ledgerEntry.setSaleOrder(saleOrder);

        EmailToSend email = EmailToSend.builder().to("accountant@msn.com").subject("Supplier Order Payment").body("The payment for the Sale Order with id " + saleOrder.getId() + " has been received.").build();
        emailService.sendMail(email);

        //save
        ledgerService.addLedger(ledgerEntry);
        //endregion
    }

    public void makePlantBike(SaleOrder saleOrder){
        Plant plant = plantRepository.findById(saleOrder.getPlant().getId()).get();
        //because we only sell one bike at a time we can safely take the first bike on the sale order items
        SaleOrderItems saleOrderItem = saleOrder.getSaleOrderItems().stream().findFirst().get();
        plantService.addPlantBike(plant, saleOrderItem.getBike(), saleOrderItem.getQuantity());

        EmailToSend email = EmailToSend.builder().to("inventory@msn.com").subject("Bike making finished").body("The Sale Order with id " + saleOrder.getId() + " has all its bikes made.").build();
        emailService.sendMail(email);

    }

    public BooleanWrapper gatherBikeParts(SaleOrder saleOrder){
        Plant plant = plantRepository.findById(saleOrder.getPlant().getId()).get();
        //because we only sell one bike at a time we can safely take the first bike on the sale order items
        SaleOrderItems saleOrderItem = saleOrder.getSaleOrderItems().stream().findFirst().get();
        BooleanWrapper result = plantService.checkSufficientParts(plant, saleOrderItem.getBike(), saleOrderItem.getQuantity());
        if(result.isResult()) {
            // plantService.addPlantBike(plant, saleOrderItem.getBike(), saleOrderItem.getQuantity());

            EmailToSend email = EmailToSend.builder().to("inventory@msn.com").subject("Bike making finished").body("The Sale Order with id " + saleOrder.getId() + " has all its bikes made.").build();
            emailService.sendMail(email);
            return new BooleanWrapper(true, "");
        }
        return new BooleanWrapper(false, result.getMessage());
    }

    public void shipBikeTransactions(SaleOrder saleOrder) {
        //get amount from po
        double amount = saleOrder.getGrandTotal();

        //region accounts
        //FIXME fetch bank and inventory accounts using enum and not id.
        int inventoryId = 13; //wrong assumption
        int accountReceivableAccountId = 10; //wrong assumption

        Account inventory = accountService.getAccount(inventoryId).get();
        Account accountReceivable = accountService.getAccount(accountReceivableAccountId).get();

        inventory.setBalance(inventory.getBalance() - amount);
        accountReceivable.setBalance(accountReceivable.getBalance() + amount);
        //endregion

        //region sale order
        //update status
        saleOrder.setShipped(true);
        //endregion

        //region ledger
        //TODO insert a ledger entry
        Ledger ledgerEntry = new Ledger();
        ledgerEntry.setDebitAccount(accountReceivable);
        ledgerEntry.setCreditAccount(inventory);
        ledgerEntry.setDate(new Date());
        ledgerEntry.setAmount(amount);
        ledgerEntry.setSaleOrder(saleOrder);

        EmailToSend email = EmailToSend.builder().to("accountant@msn.com").subject("Sale Order Shipment").body("The Sale Order with id " + saleOrder.getId() + " has shipped.").build();
        emailService.sendMail(email);

        //save
        ledgerService.addLedger(ledgerEntry);
        //endregion

        //region inventory
        //because we only sell one bike at a time we can safely take the first bike on the sale order items
        SaleOrderItems saleOrderItem = saleOrder.getSaleOrderItems().stream().findFirst().get();
        plantService.removePlantBike(saleOrder.getPlant(), saleOrderItem.getBike(), saleOrderItem.getQuantity() );
        //endregion

    }

    @Override
    public void accept(IReportGenerator reportGenerator) throws IOException
    {
        reportGenerator.generateSaleOrderReport(this);
    }
}