package com.example.springboot.service;

import com.example.springboot.entity.ContactUs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot.repository.ContactUsRepository;

import java.util.List;

@Service
public class ContactUsService {
    private ContactUsRepository contactUsRepository;

    @Autowired
    public ContactUsService(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    public List<ContactUs> getAllContact() {
        return contactUsRepository.findAll();
    }

    public ContactUs saveContact(ContactUs contactUs) {
        return contactUsRepository.save(contactUs);
    }

    public void deleteContact() {
        contactUsRepository.deleteAll();
    }

}
