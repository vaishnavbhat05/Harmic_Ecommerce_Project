package com.example.springboot.request;

import java.util.List;

import com.example.springboot.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartRequest {
    private User user;

    @JsonIgnore
    private List<CartItem> cartItem;
}
