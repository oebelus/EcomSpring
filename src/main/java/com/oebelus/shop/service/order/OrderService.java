package com.oebelus.shop.service.order;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Cart;
import com.oebelus.shop.model.Order;
import com.oebelus.shop.model.OrderItem;
import com.oebelus.shop.model.Product;
import com.oebelus.shop.repository.OrderRepository;
import com.oebelus.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.ccache.MemoryCredentialsCache;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order createOrder(Long userId) {
        return null;
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(item -> {
                    Product product = item.getProduct();
                    product.setInventory(product.getInventory() - item.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            item.getQuantity(),
                            product.getPrice()
                    );
                }).toList();
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }
}
