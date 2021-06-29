package com.soen390.erp.accounting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.soen390.erp.inventory.model.Plant;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SaleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    private Plant plant;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

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
    private boolean shipped;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_order_id")
    private Set<SaleOrderItems> saleOrderItems;

    public String toString() {
        return id+"" ;
    }
}
