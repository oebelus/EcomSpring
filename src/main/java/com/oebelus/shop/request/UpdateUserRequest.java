package com.oebelus.shop.request;

import jakarta.persistence.Entity;
import lombok.*;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
}
