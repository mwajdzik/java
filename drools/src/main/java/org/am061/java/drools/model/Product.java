package org.am061.java.drools.model;

import lombok.Data;

@Data
public class Product {

    private String type;
    private int discount;

    public Product(String type) {
        this.type = type;
    }
}