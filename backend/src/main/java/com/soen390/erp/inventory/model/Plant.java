package com.soen390.erp.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soen390.erp.accounting.model.PurchaseOrder;
import com.soen390.erp.accounting.model.SaleOrder;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String address;

    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "plantmaterial_id")
    private Set<PlantMaterial> materials;

    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "plantpart_id")
    private Set<PlantPart> parts;

    @OneToMany( fetch = FetchType.EAGER)
    @JoinColumn(name = "plantbike_id")
    private Set<PlantBike> bikes;

    @JsonIgnore
    @OneToMany(mappedBy = "plant")
    private List<PurchaseOrder> purchaseOrders;

    @JsonIgnore
    @OneToMany(mappedBy = "plant")
    private List<SaleOrder> saleOrders;

    public Optional<Set<PlantPart>> getParts() {
        return Optional.ofNullable(parts);
    }

    public Optional<Set<PlantMaterial>> getMaterials() {
        return Optional.ofNullable(materials);
    }

    public Optional<Set<PlantBike>> getBikes() {
        return Optional.ofNullable(bikes);
    }

    public void addMaterial(PlantMaterial plantMaterial){
        if (materials==null)
            materials = new HashSet<>();
        materials.add(plantMaterial);
    }

    public void addBike(PlantBike plantBike){
        if (bikes==null)
            bikes = new HashSet<>();
        bikes.add(plantBike);
    }

    public String toString() {
        return name+" "+address ;
    }

}
