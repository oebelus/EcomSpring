package com.oebelus.shop.service.order;

import com.oebelus.shop.DTO.OrderDTO;
import com.oebelus.shop.DTO.OrderItemDTO;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Transactional
    @Override
    public OrderDTO createOrder(Long userId) {
        // Retrieve the cart by user id
        Cart cart = cartService.getCartByUserId(userId);

        // Handle empty cart case
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot create order.");
        }

        // Check product availability before proceeding
        validateProductAvailability(cart);

        // Create the order and associated order items
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        // Save the order and order items
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalPrice(calculateTotalPrice(orderItemList));
        Order savedOrder = orderRepository.save(order);

        // Clear the cart, but keep the product associations intact
        cartService.clearCart(cart.getId());

        return mapToDTO(savedOrder);
    }

    // Helper method to create a new order from the cart
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    // Helper method to create order items from cart items
    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(item -> {
                    Product product = item.getProduct();
                    // Adjust product inventory
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

    // Calculate total order price
    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Ensure sufficient product inventory is available
    private void validateProductAvailability(Cart cart) {
        cart.getCartItems().forEach(item -> {
            Product product = item.getProduct();
            if (product.getInventory() < item.getQuantity()) {
                throw new IllegalArgumentException("Product " + product.getName() +
                        " has insufficient stock.");
            }
        });
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::mapToDTO).toList();
    }

    // Map Order to OrderDTO
    private OrderDTO mapToDTO(Order order) {
        Set<OrderItemDTO> orderItemDTOS = order.getOrderItems()
                .stream()
                .map(this::mapToOrderItemDTO)
                .collect(Collectors.toSet());

        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getOrderStatus(),
                orderItemDTOS
        );
    }

    // Map OrderItem to OrderItemDTO
    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getProduct()
        );
    }
}
