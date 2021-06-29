package com.soen390.erp.manufacturing.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "partType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Frame.class, name = "frame"),
        @JsonSubTypes.Type(value = Handlebar.class, name = "handlebar"),
        @JsonSubTypes.Type(value = Pedal.class, name = "pedal"),
        @JsonSubTypes.Type(value = Seat.class, name = "seat"),
        @JsonSubTypes.Type(value = Wheel.class, name = "wheel"),
        @JsonSubTypes.Type(value = Accessory.class, name = "accessory")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="product_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Part implements PartFactory{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    protected String name;
    protected double cost;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name="parts_materials",
            joinColumns=@JoinColumn(name="part_id"),
            inverseJoinColumns=@JoinColumn(name = "material_id"))
    protected Set<Material> materials;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Optional<Set<Material>> getMaterials() {
        return Optional.ofNullable(materials);
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public void addMaterial(Material material){
        if (materials==null)
            materials = new HashSet<>();
        materials.add(material);
        //it's possible that the set hasn't been initialized
        Optional<Set<Part>> partsNullable = material.getParts();
        //get a new hashset if doesn't already exist
        Set<Part> parts = partsNullable.orElseGet(HashSet::new);
        //add the current part to the materials part set
        parts.add(this);
        material.setParts(parts);
    }

}
