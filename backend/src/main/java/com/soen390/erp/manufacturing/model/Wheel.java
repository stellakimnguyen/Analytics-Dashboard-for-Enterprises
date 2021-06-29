package com.soen390.erp.manufacturing.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Entity
@DiscriminatorValue("wheel")
public class Wheel extends Part{
    protected double diameter;
    protected boolean gear;

    @Override
    public Part createPart() {
        return new Wheel();
    }
}
