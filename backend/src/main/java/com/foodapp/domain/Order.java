package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
    private Status status;
    private Date orderDate;
    private Platform orderPlatform;

    private Double totalPrice;
    private Timestamp orderTime;
    private Timestamp completionTime;

    public enum Status {
        PROCESSING, COMPLETED, CANCELLED,
    }

    public enum Platform {
        ONLINE, OFFLINE
    }
}
