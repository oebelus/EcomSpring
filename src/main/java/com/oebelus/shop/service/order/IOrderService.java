package com.oebelus.shop.service.order;

import com.oebelus.shop.model.Order;

public interface IOrderService {
    Order createOrder(Long userId);

    Order getOrder(Long orderId);
}
