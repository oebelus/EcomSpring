package com.oebelus.shop.service.order;

import com.oebelus.shop.enums.OrderStatus;
import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Cart;
import com.oebelus.shop.model.Order;
import com.oebelus.shop.model.OrderItem;
import com.oebelus.shop.model.Product;
import com.oebelus.shop.repository.OrderRepository;
import com.oebelus.shop.repository.ProductRepository;
import com.oebelus.shop.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Transactional
    @Override
    public Order createOrder(Long userId) {
        // We get the cart by the user id
        Cart cart = cartService.getCartByUserId(userId);

        // We create the order and the order items
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        // We save the order and the order items
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalPrice(calculateTotalPrice(orderItemList));
        Order savedOrder = orderRepository.save(order);

        // We clear the cart
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
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
                            item.getUnitPrice()
                    );
                }).toList();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
