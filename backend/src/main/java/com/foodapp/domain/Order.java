package com.foodapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Platform orderPlatform;
    private Double totalPrice;
    private Timestamp completionTime;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order(Status status, Platform platform, double totalPrice, Timestamp timestamp, String takeaway, User user) {
        this.status = status;
        this.orderPlatform = platform;
        this.totalPrice = totalPrice;
        this.completionTime = timestamp;
        this.description = takeaway;
        this.user = user;
    }

    public enum Status {
        PROCESSING, COMPLETED, CANCELLED,
    }

    public enum Platform {
        ONLINE, OFFLINE
    }
    // Helper method to add OrderDetail
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }
}
