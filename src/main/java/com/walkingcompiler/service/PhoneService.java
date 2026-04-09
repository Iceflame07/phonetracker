package com.walkingcompiler.service;
import com.walkingcompiler.data.models.Phone;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface PhoneService {

    Phone findByCreatePhone(String createPhone);
    String findByPhoneBrand(String phoneBrand);
    Phone findByActivePhone(String activePhone);
    Phone findByPhoneNumber(String phoneNumber);
    Optional<Phone> findByPhoneId(String phoneId);
    Phone updateByLocation(String location);
    List<Phone> findByPhoneSeenAfter(String phoneSeenAfter);
    void deletePhoneByPhoneNumber(String phoneNumber);
    Phone updatePhoneLocation(String phoneNumber, String newLocation);
    Phone countActivePhones();
    List<Phone> findPhonesSeenAfter(String phoneSeenAfter);
}
