package com.walkingcompiler.service;
import com.walkingcompiler.data.models.Phone;
import com.walkingcompiler.data.repository.PhoneRepository;
import com.walkingcompiler.exception.PhoneNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;

    @Override
    public Phone findByCreatePhone(String createPhone) {
        return phoneRepository.findByCreatePhone(createPhone);
    }

    @Override
    public String findByPhoneBrand(String phoneBrand) {
        return "";
    }

    @Override
    public Phone findByActivePhone(String activePhone) {
        return phoneRepository.findByActivePhone(activePhone);
    }

    @Override
    public Phone findByPhoneNumber(String phoneNumber) {
        return phoneRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Phone> findByPhoneId(String phoneId) {
        return Optional.ofNullable(phoneRepository.findByPhoneId(phoneId));
    }

    @Override
    public Phone updateByLocation(String location) {
        return null;
    }

    @Override
    public List<Phone> findByPhoneSeenAfter(String phoneSeenAfter) {
        return List.of();
    }

    @Override
    public void deletePhoneByPhoneNumber(String phoneNumber) {
        if (!phoneRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNotFoundException("Phone with number " + phoneNumber + " not found for deletion.");
        }
        phoneRepository.deleteByPhoneNumber(phoneNumber);
    }

    @Override
    public Phone updatePhoneLocation(String phoneNumber, String newLocation) {
        return phoneRepository.findByPhoneNumber(phoneNumber)
                .map(phone -> {
                    phone.setLocation(newLocation);
                    return phoneRepository.save(phone);
                })
                .orElseThrow(() -> new PhoneNotFoundException("Phone with number " + phoneNumber + " not found for update."));
    }

    @Override
    public Phone countActivePhones() {
        return phoneRepository.findByActivePhone(String.valueOf(true));
    }

    @Override
    public List<Phone> findPhonesSeenAfter(String phoneSeenAfter) {
        return List.of();
    }
}