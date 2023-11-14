package com.example.springboot.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ResetPasswordRequest {
    private String resetToken;
    private String newPassword;
}