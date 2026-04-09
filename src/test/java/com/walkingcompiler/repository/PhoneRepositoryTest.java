package com.walkingcompiler.repository;

import com.walkingcompiler.data.models.Phone;
import com.walkingcompiler.data.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataMongoTest
@ActiveProfiles("test")
class PhoneRepositoryTest {

    @Autowired
    private PhoneRepository phoneRepository;

    private Phone testPhone;

    @BeforeEach
    void setUp() {
        phoneRepository.deleteAll();
        testPhone = new Phone("1234567890", "Apple", "iPhone 14", 40.7128, -74.0060);
    }

    @Test
    void shouldSaveAndFindPhoneById() {
        Phone savedPhone = phoneRepository.save(testPhone);
        
        Optional<Phone> foundPhone = phoneRepository.findById(savedPhone.getId());
        
        Assertions.assertTrue(foundPhone.isPresent());
        Assertions.assertEquals(testPhone.getPhoneNumber(), foundPhone.get().getPhoneNumber());
        Assertions.assertEquals(testPhone.getBrand(), foundPhone.get().getBrand());
    }

    @Test
    void shouldFindPhoneByPhoneNumber() {
        phoneRepository.save(testPhone);
        
        Optional<Phone> foundPhone = phoneRepository.findByPhoneNumber(testPhone.getPhoneNumber());
        
        Assertions.assertTrue(foundPhone.isPresent());
        Assertions.assertEquals(testPhone.getPhoneNumber(), foundPhone.get().getPhoneNumber());
    }

    @Test
    void shouldFindActivePhones() {
        Phone activePhone = phoneRepository.save(testPhone);
        Phone inactivePhone = new Phone("0987654321", "Samsung", "Galaxy S23", 34.0522, -118.2437);
        inactivePhone.setIsActive(false);
        phoneRepository.save(inactivePhone);
        
        List<Phone> activePhones = phoneRepository.findByIsActiveTrue();
        
        Assertions.assertEquals(1, activePhones.size());
        Assertions.assertEquals(activePhone.getId(), activePhones.get(0).getId());
    }

    @Test
    void shouldFindPhonesByBrand() {
        phoneRepository.save(testPhone);
        Phone anotherApplePhone = new Phone("1111111111", "Apple", "iPhone 13", 41.8781, -87.6298);
        phoneRepository.save(anotherApplePhone);
        Phone samsungPhone = new Phone("2222222222", "Samsung", "Galaxy S23", 34.0522, -118.2437);
        phoneRepository.save(samsungPhone);
        
        List<Phone> applePhones = phoneRepository.findByBrand("Apple");
        
        Assertions.assertEquals(2, applePhones.size());
        Assertions.assertTrue(applePhones.stream().allMatch(phone -> "Apple".equals(phone.getBrand())));
    }

    @Test
    void shouldDeletePhone() {
        Phone savedPhone = phoneRepository.save(testPhone);
        
        phoneRepository.deleteById(savedPhone.getId());
        
        Optional<Phone> deletedPhone = phoneRepository.findById(savedPhone.getId());
        Assertions.assertFalse(deletedPhone.isPresent());
    }
}
