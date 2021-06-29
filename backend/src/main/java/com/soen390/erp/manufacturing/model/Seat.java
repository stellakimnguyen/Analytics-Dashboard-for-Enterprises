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
@DiscriminatorValue("seat")
public class Seat extends Part{
    protected int size;

    @Override
    public Part createPart() {
        return new Seat();
    }
}

