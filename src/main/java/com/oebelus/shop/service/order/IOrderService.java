package com.oebelus.shop.service.order;

import com.oebelus.shop.DTO.OrderDTO;
import com.oebelus.shop.model.Order;

import java.util.List;

public interface IOrderService {
    OrderDTO createOrder(Long userId);

    OrderDTO getOrder(Long orderId);

    List<OrderDTO> getOrdersByUserId(Long userId);
}
