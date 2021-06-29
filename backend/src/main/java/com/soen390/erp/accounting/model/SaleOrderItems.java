package com.soen390.erp.accounting.model;

import com.soen390.erp.manufacturing.model.Bike;
import lombok.*;

import javax.persistence.*;

// TODO: to be deleted if we are ordering one type of bike per Client Order(e.g. the customer wants 2 pink bikes and 1 blue bike => 2 Client Orders)
// @see con.soen390.erp.inventory.model.ClientOrder

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SaleOrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private double unitPrice;

    @ManyToOne
    @JoinColumn(name = "bike_id")
    private Bike bike;

    public String toString() {
        return bike+"" ;
    }
}
