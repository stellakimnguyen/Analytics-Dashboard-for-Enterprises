package com.soen390.erp.inventory.model;

import com.soen390.erp.manufacturing.model.Part;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PlantPart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "part_id")
    private Part part;

    private int quantity;
}
