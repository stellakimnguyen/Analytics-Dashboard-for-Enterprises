package com.soen390.erp.manufacturing.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@DiscriminatorValue("frame")
public class Frame extends Part{
    @Override
    public Part createPart() {
        return new Frame();
    }

    enum Colour {RED, BLUE, GREEN, ORANGE, SILVER, BLACK}
    enum Finish {MATTE, CHROME}

    protected Colour colour;
    protected double size;
    protected Finish finish;

}
