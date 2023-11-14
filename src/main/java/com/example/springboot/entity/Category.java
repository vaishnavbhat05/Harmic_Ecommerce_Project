package com.example.springboot.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "cat_tbl")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Long id;

    @Column(name = "catName", unique = true,nullable = false)
    private String catName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    @JsonBackReference(value = "products-category")
    private List<Products> products;
}
