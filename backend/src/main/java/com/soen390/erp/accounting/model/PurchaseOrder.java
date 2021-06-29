package com.soen390.erp.accounting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.soen390.erp.inventory.model.Plant;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * this gets created and one record gets inserted everytime we order material from a supplier
 */


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    private Plant plant;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    /**
     * before discount and tax
     */
    private double totalAmount;
    /**
     * in percentage e.g. for 10% discount record 10
     */
    private double discount;
    /**
     * = totalAmount * discount
     */
    private double discountAmount;
    /**
     * in percentage e.g. for 15% tax record 15
     */
    private double tax;
    /**
     * = totalAmount * tax
     */
    private double taxAmount;
    /**
     * after discount and tax = totalAmount - discountAmount + taxAmount
     */
    private double grandTotal;

    private boolean paid;
    private boolean received;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
    private Set<PurchaseOrderItems> purchaseOrderItems;

    public String toString() {
        return id+"" ;
    }
}
