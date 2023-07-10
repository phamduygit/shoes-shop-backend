package com.shoesshop.backend.service;

import com.shoesshop.backend.dto.CartItemResponse;
import com.shoesshop.backend.dto.OrderRequest;
import com.shoesshop.backend.dto.OrderResponse;
import com.shoesshop.backend.dto.RatingRequest;
import com.shoesshop.backend.entity.CartItem;
import com.shoesshop.backend.entity.Order;
import com.shoesshop.backend.entity.User;
import com.shoesshop.backend.exception.DuplicateEntryException;
import com.shoesshop.backend.exception.NotFoundException;
import com.shoesshop.backend.repository.CartItemRepository;
import com.shoesshop.backend.repository.OrderRepository;
import com.shoesshop.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public Map<String, Object> getNotCompleteOrderList(Order.ShippingStatus shippingStatus, int pageNumber, int pageSize) {
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Order> orders = orderRepository.findAllByUserIdAndShippingStatus(user.getId(), shippingStatus, page);

        List<OrderResponse> listOrderResponse = new ArrayList<>();
        for (Order order : orders.getContent()) {
            OrderResponse orderResponse = new OrderResponse(order);
            listOrderResponse.add(orderResponse);
        }
        responseResult.put("hasNextPage", orders.hasNext());
        responseResult.put("hasPreviousPage", orders.hasPrevious());
        responseResult.put("totalPages", orders.getTotalPages());
        responseResult.put("length", listOrderResponse.size());
        responseResult.put("list", listOrderResponse);
        return responseResult;
    }

    public Map<String, Object> getOrderList() {
        Map<String, Object> responseResult = new LinkedHashMap<>();
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> listOrderResponse = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse(order);
            listOrderResponse.add(orderResponse);
        }
        responseResult.put("length", listOrderResponse.size());
        responseResult.put("list", listOrderResponse);
        return responseResult;
    }

    public Map<String, Object> createOrder(OrderRequest orderRequest) {
        Map<String, Object> responseResult = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email: " + authentication.getName()));
        List<CartItem> items = cartItemRepository.findAllByCartId(orderRequest.getCartId());
        List<OrderResponse> listOrderResponse = new ArrayList<>();
        for (CartItem item : items) {
            Order order = Order.builder()
                    .user(user)
                    .address(orderRequest.getAddress())
                    .shoes(item.getShoes())
                    .size(item.getSize())
                    .quantity(item.getQuantity())
                    .shippingMethod(orderRequest.getShippingMethod())
                    .paymentMethod(orderRequest.getPaymentMethod())
                    .shippingStatus(Order.ShippingStatus.PREPARE)
                    .build();
            Order savedOrder = orderRepository.save(order);
            OrderResponse orderResponse = new OrderResponse(savedOrder);
            listOrderResponse.add(orderResponse);
        }
        responseResult.put("length", listOrderResponse.size());
        responseResult.put("list", listOrderResponse);
        // Delete cart with id
        return responseResult;
    }

    public OrderResponse updateShippingStatus(int orderId, Order.ShippingStatus shippingStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found by id: " + orderId));
        order.setShippingStatus(shippingStatus);
        Order updatedOrder = orderRepository.save(order);
        return new OrderResponse(updatedOrder);
    }

    public OrderResponse addRatingAndComment(int orderId, RatingRequest ratingRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found by id: " + orderId));
        if (order.getShippingStatus().name() != "COMPLETE") {
            throw new NotFoundException("The order not completed");
        }
        order.setRating(ratingRequest.getRating());
        order.setComment(ratingRequest.getComment());
        Order updatedOrder = orderRepository.save(order);
        return new OrderResponse(updatedOrder);
    }


}
