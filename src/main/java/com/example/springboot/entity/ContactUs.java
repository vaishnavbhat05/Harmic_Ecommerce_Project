package com.example.springboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contactus_tbl")
public class ContactUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    @Column(name = "phone",nullable = false)
    private Long phone;

    @Column(name = "whatsapp",nullable = false)
    private Long whatsapp;

    @Column(name = "email",nullable = false)
    private String email;
}
