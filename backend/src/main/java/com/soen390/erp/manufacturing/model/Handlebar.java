package com.soen390.erp.manufacturing.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@DiscriminatorValue("handlebar")
public class Handlebar extends Part{
    @Override
    public Part createPart() {
        return new Handlebar();
    }

    enum Type {DROPBAR, STRAIGHT, BULLHORN}

    protected Type type;
}
