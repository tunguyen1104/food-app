package com.foodapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail extends BaseEntity {
    private Double unitPrice;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
