package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.Food;
import com.foodapp.domain.Order;
import com.foodapp.domain.OrderDetail;
import com.foodapp.dto.requests.OrderDetailRequest;
import com.foodapp.dto.response.OrderDetailResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.OrderDetailMapper;
import com.foodapp.repositories.OrderDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService {

    OrderDetailRepository orderDetailRepository;
    OrderService orderService;
    OrderDetailMapper orderDetailMapper;
    FoodService foodService;

    @Transactional
    public List<OrderDetailResponse> getDetailsByOrderId(Long orderId) {
        Order order = orderService.getOrderEntityById(orderId);

        List<OrderDetail> details = order.getOrderDetails();

        return details.stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDetailResponse getDetailById(Long id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
        return orderDetailMapper.toOrderDetailResponse(detail);
    }

    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        Order order = orderService.getOrderEntityById(request.getOrderId());

        Food food = foodService.getFoodEntityById(request.getFoodId());

        OrderDetail detail = OrderDetail.builder()
                .order(order)
                .food(food)
                .quantity(request.getQuantity())
                .build();

        orderDetailRepository.save(detail);
        return orderDetailMapper.toOrderDetailResponse(detail);
    }

    @Transactional
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailRequest request) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        detail.setQuantity(request.getQuantity());
        orderDetailRepository.save(detail);

        return orderDetailMapper.toOrderDetailResponse(detail);
    }

    @Transactional
    public void deleteOrderDetail(Long id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
        orderDetailRepository.delete(detail);
    }
}
