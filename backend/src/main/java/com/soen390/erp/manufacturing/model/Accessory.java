package com.soen390.erp.manufacturing.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@DiscriminatorValue("accessory")
public class Accessory extends Part{
    @Override
    public Part createPart() {
        return new Accessory();
    }
}
