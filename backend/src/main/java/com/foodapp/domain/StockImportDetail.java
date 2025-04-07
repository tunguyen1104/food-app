package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_stock_import_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockImportDetail extends BaseEntity {
    private String description;
}
