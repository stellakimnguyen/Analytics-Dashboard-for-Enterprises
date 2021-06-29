package com.soen390.erp.inventory.model;

import com.soen390.erp.manufacturing.model.Material;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PlantMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @Min(value = 1)
    private int quantity;

    @Override
    public String toString() {
        return "PlantMaterial{" +
                "id=" + id +
                ", material=" + material +
                ", quantity=" + quantity +
                '}';
    }
}
