package com.oebelus.shop.service.user;

import com.oebelus.shop.DTO.*;
import com.oebelus.shop.exceptions.AlreadyExistsException;
import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.*;
import com.oebelus.shop.repository.UserRepository;
import com.oebelus.shop.request.CreateUserRequest;
import com.oebelus.shop.request.UpdateUserRequest;
import com.oebelus.shop.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(request.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        return userRepository.findById(userId).map(user -> {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    @Override
    public UserDTO userToDTO(User user) {
        List<OrderDTO> ordersDTO = orderService.getOrdersByUserId(user.getId());
        CartDTO cart = mapToCartDTO(user.getCart());

        Long userId = user.getId();
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                ordersDTO,
                cart
        );
    }

    private CartDTO mapToCartDTO(Cart cart) {
        Set<CartItemDTO> cartItemsDTO = cart.getCartItems().stream().map(this::mapToCartItemDTO).collect(Collectors.toSet());
        return new CartDTO(
                cart.getId(),
                cartItemsDTO,
                cart.getTotalPrice()
        );
    }

    private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
        ProductDTO productDTO = mapToProductDTO(cartItem.getProduct());

        return new CartItemDTO(
                cartItem.getId(),
                cartItem.getUnitPrice(),
                cartItem.getTotalPrice(),
                cartItem.getQuantity(),
                productDTO
        );
    }

    private ProductDTO mapToProductDTO(Product product) {
        List<ImageDTO> imagesDTO = product.getImages().stream().map(this::mapToImageDTO).toList();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getInventory(),
                product.getPrice(),
                product.getCategory(),
                imagesDTO
        );
    }

    private ImageDTO mapToImageDTO(Image image) {
        return new ImageDTO(
                image.getId(),
                image.getFileName(),
                image.getDownloadUrl()
        );
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email);
    }
}
