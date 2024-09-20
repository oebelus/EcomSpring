package com.oebelus.shop.controller;

import com.oebelus.shop.DTO.UserDTO;
import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.User;
import com.oebelus.shop.request.CreateUserRequest;
import com.oebelus.shop.request.UpdateUserRequest;
import com.oebelus.shop.response.ApiResponse;
import com.oebelus.shop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userService.userToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User retrieved successfully", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDTO userDTO = userService.userToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User registered successfully", userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PutMapping("{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userService.userToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}
