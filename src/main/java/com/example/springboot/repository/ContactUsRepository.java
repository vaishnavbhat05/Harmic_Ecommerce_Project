package com.example.springboot.repository;

import com.example.springboot.entity.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepository extends JpaRepository<ContactUs,Long> {
}
