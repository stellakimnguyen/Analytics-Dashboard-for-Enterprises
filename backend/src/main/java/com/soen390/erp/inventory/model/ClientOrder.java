package com.soen390.erp.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.soen390.erp.accounting.model.Client;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ClientOrder {

    // order id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // client id, an order can belong to only one client. many orders can belong to a client.
    @ManyToOne (fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // to fix serializable issue
    @JoinColumn(name = "client_id")
    private Client client;

    // cost
    private double cost;

    // quantity
    private int quantity;

    // assuming we cannot re-order the same bike id
    // bike id, one order belongs to only one bike
    @OneToOne (optional = false)
    @JoinColumn(name = "plantBike_id")
    private PlantBike plantBike;
}
