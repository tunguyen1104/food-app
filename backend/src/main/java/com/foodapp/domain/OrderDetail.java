package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_order_detail")
@Getter
@Setter
@NoArgsConstructor
public class OrderDetail extends BaseEntity {
}
