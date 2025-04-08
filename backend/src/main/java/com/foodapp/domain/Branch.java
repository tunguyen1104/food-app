package com.foodapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_branch")
@Getter
@Setter
public class Branch extends BaseEntity {
}
