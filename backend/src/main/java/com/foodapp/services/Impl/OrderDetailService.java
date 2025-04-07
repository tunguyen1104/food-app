package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Order;
import com.foodapp.domain.OrderDetail;
import com.foodapp.dto.response.OrderDetailResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.OrderDetailMapper;
import com.foodapp.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService {

    OrderRepository orderRepository;
    OrderDetailMapper orderDetailMapper;

    public List<OrderDetailResponse> getDetailsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderDetail> details = order.getOrderDetails();

        return details.stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .collect(Collectors.toList());
    }
}

