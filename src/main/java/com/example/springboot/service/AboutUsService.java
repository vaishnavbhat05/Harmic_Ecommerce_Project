package com.example.springboot.service;

import com.example.springboot.entity.AboutUs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot.repository.AboutUsRepository;

import java.util.List;

@Service
public class AboutUsService {
    private AboutUsRepository aboutUsRepository;

    @Autowired
    public AboutUsService(AboutUsRepository aboutUsRepository) {
        this.aboutUsRepository = aboutUsRepository;
    }

    public List<AboutUs> getAllAboutUs() {
        return aboutUsRepository.findAll();
    }

    public AboutUs saveAboutUs(AboutUs aboutUs) {
        return aboutUsRepository.save(aboutUs);
    }

    public void deleteAboutUs() {
        aboutUsRepository.deleteAll();
    }

}
