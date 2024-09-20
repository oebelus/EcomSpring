package com.oebelus.shop.DTO;

import com.oebelus.shop.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDTO> orders;
    private CartDTO cart;
}
