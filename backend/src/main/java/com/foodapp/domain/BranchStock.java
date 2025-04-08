package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_branch_stock")
@Getter
@Setter
public class BranchStock extends BaseEntity {
}
