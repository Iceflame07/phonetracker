package com.walkingcompiler.service;

import com.walkingcompiler.dto.PhoneDto;
import com.walkingcompiler.exception.DuplicatePhoneException;
import com.walkingcompiler.exception.PhoneNotFoundException;
import com.walkingcompiler.data.repository.PhoneRepository;
import com.walkingcompiler.utils.PhoneMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class PhoneServiceTest {

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhoneMapper phoneMapper;

    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        phoneRepository.deleteAll();
        testPhoneDto = new PhoneDto("1234567890", "Apple", "iPhone 14", 40.7128, -74.0060);
    }

    @Test
    void shouldCreatePhoneSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        
        Assertions.assertNotNull(createdPhone.getId());
        Assertions.assertEquals(testPhoneDto.getPhoneNumber(), createdPhone.getPhoneNumber());
        Assertions.assertEquals(testPhoneDto.getBrand(), createdPhone.getBrand());
        Assertions.assertEquals(testPhoneDto.getModel(), createdPhone.getModel());
        Assertions.assertEquals(testPhoneDto.getLatitude(), createdPhone.getLatitude());
        Assertions.assertEquals(testPhoneDto.getLongitude(), createdPhone.getLongitude());
        Assertions.assertTrue(createdPhone.getIsActive());
        Assertions.assertNotNull(createdPhone.getLastSeen());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingPhoneWithNullFields() {
        PhoneDto invalidPhone = new PhoneDto(null, "Apple", "iPhone", 40.0, -74.0);
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            phoneService.createPhone(invalidPhone);
        });
    }

    @Test
    void shouldThrowDuplicatePhoneExceptionWhenCreatingDuplicatePhone() {
        phoneService.createPhone(testPhoneDto);
        
        Assertions.assertThrows(DuplicatePhoneException.class, () -> {
            phoneService.createPhone(testPhoneDto);
        });
    }

    @Test
    void shouldGetPhoneByIdSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        
        PhoneDto foundPhone = phoneService.getPhoneById(createdPhone.getId());
        
        Assertions.assertEquals(createdPhone.getId(), foundPhone.getId());
        Assertions.assertEquals(createdPhone.getPhoneNumber(), foundPhone.getPhoneNumber());
    }

    @Test
    void shouldThrowPhoneNotFoundExceptionWhenGettingNonExistentPhone() {
        Assertions.assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.getPhoneById("nonexistent");
        });
    }

    @Test
    void shouldGetPhoneByNumberSuccessfully() {
        phoneService.createPhone(testPhoneDto);
        
        PhoneDto foundPhone = phoneService.getPhoneByNumber(testPhoneDto.getPhoneNumber());
        
        Assertions.assertEquals(testPhoneDto.getPhoneNumber(), foundPhone.getPhoneNumber());
    }

    @Test
    void shouldGetActivePhonesSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        
        List<PhoneDto> activePhones = phoneService.getActivePhones();
        
        Assertions.assertEquals(1, activePhones.size());
        Assertions.assertTrue(activePhones.get(0).getIsActive());
    }

    @Test
    void shouldGetPhonesByBrandSuccessfully() {
        phoneService.createPhone(testPhoneDto);
        PhoneDto anotherApplePhone = new PhoneDto("1111111111", "Apple", "iPhone 13", 41.8781, -87.6298);
        phoneService.createPhone(anotherApplePhone);
        PhoneDto samsungPhone = new PhoneDto("2222222222", "Samsung", "Galaxy S23", 34.0522, -118.2437);
        phoneService.createPhone(samsungPhone);
        
        List<PhoneDto> applePhones = phoneService.getPhonesByBrand("Apple");
        
        Assertions.assertEquals(2, applePhones.size());
        Assertions.assertTrue(applePhones.stream().allMatch(phone -> "Apple".equals(phone.getBrand())));
    }

    @Test
    void shouldUpdatePhoneSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        
        createdPhone.setBrand("Samsung");
        createdPhone.setModel("Galaxy S23");
        
        PhoneDto updatedPhone = phoneService.updatePhone(createdPhone.getId(), createdPhone);
        
        Assertions.assertEquals("Samsung", updatedPhone.getBrand());
        Assertions.assertEquals("Galaxy S23", updatedPhone.getModel());
    }

    @Test
    void shouldThrowPhoneNotFoundExceptionWhenUpdatingNonExistentPhone() {
        Assertions.assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.updatePhone("nonexistent", testPhoneDto);
        });
    }

    @Test
    void shouldDeletePhoneSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        
        phoneService.deletePhone(createdPhone.getId());
        
        Assertions.assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.getPhoneById(createdPhone.getId());
        });
    }

    @Test
    void shouldThrowPhoneNotFoundExceptionWhenDeletingNonExistentPhone() {
        Assertions.assertThrows(PhoneNotFoundException.class, () -> {
            phoneService.deletePhone("nonexistent");
        });
    }

    @Test
    void shouldUpdateLocationSuccessfully() {
        PhoneDto createdPhone = phoneService.createPhone(testPhoneDto);
        Double newLatitude = 51.5074;
        Double newLongitude = -0.1278;
        
        PhoneDto updatedPhone = phoneService.updateLocation(createdPhone.getId(), newLatitude, newLongitude);
        
        Assertions.assertEquals(newLatitude, updatedPhone.getLatitude());
        Assertions.assertEquals(newLongitude, updatedPhone.getLongitude());
        Assertions.assertNotNull(updatedPhone.getLastSeen());
    }
}
