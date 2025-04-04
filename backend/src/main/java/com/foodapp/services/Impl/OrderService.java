package com.foodapp.services.Impl;

import com.foodapp.dto.response.OrderResponse;
import com.foodapp.mapper.OrderMapper;
import com.foodapp.repositories.OrderRepository;
import com.foodapp.repositories.UserRepository;
import com.foodapp.utils.AuthenticationFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    AuthenticationFacade authenticationFacade;

    public List<OrderResponse> getOrdersForCurrentUser() {
        Long userId = authenticationFacade.getAuthenticatedUser().getId();
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
