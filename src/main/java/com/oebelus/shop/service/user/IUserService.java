package com.oebelus.shop.service.user;

import com.oebelus.shop.DTO.UserDTO;
import com.oebelus.shop.model.User;
import com.oebelus.shop.request.CreateUserRequest;
import com.oebelus.shop.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(Long userId, UpdateUserRequest request);

    void deleteUser(Long userId);

    UserDTO userToDTO(User user);
}
