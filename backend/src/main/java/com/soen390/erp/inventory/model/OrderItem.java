package com.soen390.erp.inventory.model;

import com.soen390.erp.manufacturing.model.Material;

import javax.persistence.*;

@Entity
@IdClass(OrderItemId.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "material_id")
    Material material;

    @ManyToOne
    @JoinColumn(name = "supplier_order_id")
    SupplierOrder supplierOrder;

    private int quantity;

}
