package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_stock_import")
@Getter
@Setter
@Builder
public class StockImport extends BaseEntity {
    private String description;
}
