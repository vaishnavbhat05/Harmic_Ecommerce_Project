package com.example.springboot.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {

    private String email;

    public PasswordRequest(@JsonProperty("email") String email){
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "PasswordRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}
