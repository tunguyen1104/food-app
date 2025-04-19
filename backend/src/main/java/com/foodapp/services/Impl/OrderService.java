package com.foodapp.services.Impl;

import com.foodapp.constants.ErrorCode;
import com.foodapp.domain.*;
import com.foodapp.dto.requests.OrderDetailRequest;
import com.foodapp.dto.requests.OrderRequest;
import com.foodapp.dto.response.OrderResponse;
import com.foodapp.exceptions.AppException;
import com.foodapp.mapper.OrderMapper;
import com.foodapp.repositories.OrderDetailRepository;
import com.foodapp.repositories.OrderRepository;
import com.foodapp.utils.AuthenticationFacade;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    AuthenticationFacade authenticationFacade;
    FoodService foodService;
    OrderDetailRepository orderDetailRepository;
    OrderMapper orderMapper;
    UserService userService;

    @Transactional
    public List<OrderResponse> getOrdersForCurrentUser() {
        Long userId = authenticationFacade.getAuthenticatedUser().getId();
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> getMyOrdersByStatus(Order.Status status) {
        Long userId = authenticationFacade.getAuthenticatedUser().getId();
        List<Order> orders = orderRepository.findAllByUserIdAndStatus(userId, status);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long orderId, String newStatusStr) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        Order.Status newStatus;
        try {
            newStatus = Order.Status.valueOf(newStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(updatedOrder);
    }

    public Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = buildOrder(orderRequest);

        orderRepository.save(order);

        saveOrderDetails(orderRequest.getOrderDetails(), order);

        return orderMapper.toOrderResponse(this.getOrderEntityById(order.getId()));
    }

    private Order buildOrder(OrderRequest req) {
        User user = userService.findByIdOrThrow(req.getUserId());

        Timestamp completion = Timestamp.valueOf(
                LocalDateTime.now().plusMinutes(30));

        return Order.builder()
                .orderPlatform(Order.Platform.valueOf(req.getOrderPlatform()))
                .status(Order.Status.valueOf(req.getStatus()))
                .totalPrice(req.getTotalPrice())
                .completionTime(completion)
                .description(req.getDescription())
                .user(user)
                .build();
    }

    private void saveOrderDetails(List<OrderDetailRequest> orderDetailRequests, Order order) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(detailRequest -> {

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(detailRequest.getQuantity());
            orderDetail.setOrder(order);

            Food food = foodService.getFoodEntityById(detailRequest.getFoodId());

            orderDetail.setFood(food);
            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        orderDetailRepository.saveAll(orderDetails);
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest request) {
        Order order = this.getOrderEntityById(orderId);

        order.setStatus(Order.Status.valueOf(request.getStatus()));
        order.setOrderPlatform(Order.Platform.valueOf(request.getOrderPlatform()));
        order.setTotalPrice(request.getTotalPrice());
        order.setDescription(request.getDescription());

        orderDetailRepository.deleteAll(order.getOrderDetails());

        List<OrderDetail> newDetails = request.getOrderDetails().stream().map(detailRequest -> {
            Food food = foodService.getFoodEntityById(detailRequest.getFoodId());
            return OrderDetail.builder()
                    .order(order)
                    .food(food)
                    .quantity(detailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        order.setOrderDetails(newDetails);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = this.getOrderEntityById(orderId);
        orderRepository.delete(order);
    }
}
