package com.foodapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    public static String MANAGER = "MANAGER";
    public static String EMPLOYEE = "EMPLOYEE";
    @Column(name = "name", nullable = false)
    public String name;
}