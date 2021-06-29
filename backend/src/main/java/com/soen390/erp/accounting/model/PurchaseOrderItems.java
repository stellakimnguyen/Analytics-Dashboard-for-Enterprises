package com.soen390.erp.accounting.model;

import com.soen390.erp.manufacturing.model.Material;
import lombok.*;

import javax.persistence.*;

/**
 * this get created for each item in the PurchaseOrder class
 * @see com.soen390.erp.accounting.model.PurchaseOrder
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseOrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private double unitPrice;

    @ManyToOne
    @JoinColumn(name = "material_id")
    Material material;

    public String toString(){
        return material+"";
    }
}
