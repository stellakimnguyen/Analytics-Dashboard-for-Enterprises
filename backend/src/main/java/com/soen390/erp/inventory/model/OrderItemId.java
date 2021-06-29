package com.soen390.erp.inventory.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemId implements Serializable {

    int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OrderItemId that = (OrderItemId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
