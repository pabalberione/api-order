package com.api_order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Ordenes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String product;
    private long quantity;
    @Column(name = "total_price")
    private double totalPrice;
    @Column(name = "order_date")
    private Date orderDate;
}
