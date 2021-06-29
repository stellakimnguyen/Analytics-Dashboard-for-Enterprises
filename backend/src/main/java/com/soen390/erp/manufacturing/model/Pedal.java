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
@DiscriminatorValue("pedal")
public class Pedal extends Part{

    enum Type {STRAP, CLIP}

    protected Type type;

    @Override
    public Part createPart() {
        return new Pedal();
    }
}
