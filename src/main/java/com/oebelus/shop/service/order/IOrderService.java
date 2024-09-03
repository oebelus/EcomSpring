package com.oebelus.shop.service.order;

import com.oebelus.shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(Long userId);

    Order getOrder(Long orderId);

    List<Order> getOrdersByUserId(Long userId);
}
