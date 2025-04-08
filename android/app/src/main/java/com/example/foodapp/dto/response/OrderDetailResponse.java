package com.example.foodapp.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse implements Serializable{
    private Long id;
    private Double unitPrice;
    private Integer quantity;
    private String foodName;
    private String categoryName;
    private String avatarUrl;
}