package com.soen390.erp.manufacturing.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Bike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name="handlebar_id")
    private Handlebar handlebar;
    @ManyToOne(optional = false)
    @JoinColumn(name="frame_id")
    private Frame frame;
    @ManyToOne(optional = false)
    @JoinColumn(name="frontwheel_id")
    private Wheel frontwheel;
    @ManyToOne(optional = false)
    @JoinColumn(name="rearwheel_id")
    private Wheel rearwheel;
    @ManyToOne(optional = false)
    @JoinColumn(name="seat_id")
    private Seat seat;
    @ManyToOne(optional = false)
    @JoinColumn(name="pedal_id")
    private Pedal pedal;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "bike_accesories",
            joinColumns = { @JoinColumn(name = "bike_id") },
            inverseJoinColumns = { @JoinColumn(name = "accessory_id") })
    private Set<Accessory> accessories;

    public String toString() {
        return name+"" ;
    }
}
