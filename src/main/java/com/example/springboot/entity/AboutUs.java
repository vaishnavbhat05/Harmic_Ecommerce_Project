package com.example.springboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "aboutus_tbl")
public class AboutUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "about_id")
    private Long id;

    @Column(name = "about_us",nullable = false,length= 1000)

    private String about;

}
