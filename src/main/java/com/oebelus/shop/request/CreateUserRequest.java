package com.oebelus.shop.request;

import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
