package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_stock_import_detail")
@Getter
@Setter
@Builder
public class StockImportDetail extends BaseEntity {
}
