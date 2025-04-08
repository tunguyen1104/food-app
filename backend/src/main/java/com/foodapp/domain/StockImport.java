package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_stock_import")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockImport extends BaseEntity {
    private String description;
}
