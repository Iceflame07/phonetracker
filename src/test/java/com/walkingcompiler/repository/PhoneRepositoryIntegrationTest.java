package com.walkingcompiler.repository;

import com.walkingcompiler.data.models.Phone;
import com.walkingcompiler.data.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class PhoneRepositoryIntegrationTest {

    @Autowired
    private PhoneRepository phoneRepository;

    private Phone testPhone;

    @BeforeEach
    void setUp() {
        phoneRepository.deleteAll();
        testPhone = new Phone("1234567890", "Apple", "iPhone 14", 40.7128, -74.0060);
    }

    @Test
    void shouldConnectToMongoDBAndSavePhone() {
        Phone savedPhone = phoneRepository.save(testPhone);
        
        Assertions.assertNotNull(savedPhone.getId());
        Assertions.assertEquals(testPhone.getPhoneNumber(), savedPhone.getPhoneNumber());
        
        Optional<Phone> foundPhone = phoneRepository.findById(savedPhone.getId());
        Assertions.assertTrue(foundPhone.isPresent());
        Assertions.assertEquals(testPhone.getPhoneNumber(), foundPhone.get().getPhoneNumber());
    }

    @Test
    void shouldFindActivePhonesbyBrand() {
        phoneRepository.save(testPhone);
        Phone inactiveApplePhone = new Phone("9876543210", "Apple", "iPhone 13", 41.0, -75.0);
        inactiveApplePhone.setIsActive(false);
        phoneRepository.save(inactiveApplePhone);
        
        List<Phone> activeApplePhones = phoneRepository.findActivePhonesbyBrand("Apple");
        
        Assertions.assertEquals(1, activeApplePhones.size());
        Assertions.assertTrue(activeApplePhones.get(0).getIsActive());
        Assertions.assertEquals("Apple", activeApplePhones.get(0).getBrand());
    }

    @Test
    void shouldSearchPhonesByNumberPattern() {
        phoneRepository.save(testPhone);
        Phone anotherPhone = new Phone("1234999999", "Samsung", "Galaxy", 42.0, -76.0);
        phoneRepository.save(anotherPhone);
        
        List<Phone> foundPhones = phoneRepository.findByPhoneNumberContaining("1234");
        
        Assertions.assertEquals(2, foundPhones.size());
    }

    @Test
    void shouldFindPhonesSeenAfterDateTime() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(1);
        
        phoneRepository.save(testPhone);
        
        List<Phone> recentPhones = phoneRepository.findPhonesSeenAfter(cutoffTime);
        
        Assertions.assertEquals(1, recentPhones.size());
        Assertions.assertTrue(recentPhones.get(0).getLastSeen().isAfter(cutoffTime));
    }

    @Test
    void shouldCountPhonesByBrand() {
        phoneRepository.save(testPhone);
        Phone anotherApplePhone = new Phone("5555555555", "Apple", "iPhone 13", 43.0, -77.0);
        phoneRepository.save(anotherApplePhone);
        Phone samsungPhone = new Phone("6666666666", "Samsung", "Galaxy", 44.0, -78.0);
        phoneRepository.save(samsungPhone);
        
        long appleCount = phoneRepository.countByBrand("Apple");
        long samsungCount = phoneRepository.countByBrand("Samsung");
        
        Assertions.assertEquals(2, appleCount);
        Assertions.assertEquals(1, samsungCount);
    }

    @Test
    void shouldCountActivePhones() {
        phoneRepository.save(testPhone);
        Phone inactivePhone = new Phone("7777777777", "OnePlus", "11", 45.0, -79.0);
        inactivePhone.setIsActive(false);
        phoneRepository.save(inactivePhone);
        
        long activeCount = phoneRepository.countByIsActiveTrue();
        
        Assertions.assertEquals(1, activeCount);
    }

    @Test
    void shouldCheckPhoneNumberExists() {
        phoneRepository.save(testPhone);
        
        boolean exists = phoneRepository.existsByPhoneNumber("1234567890");
        boolean notExists = phoneRepository.existsByPhoneNumber("0000000000");
        
        Assertions.assertTrue(exists);
        Assertions.assertFalse(notExists);
    }
}
