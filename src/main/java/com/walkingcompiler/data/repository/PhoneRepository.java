package com.walkingcompiler.data.repository;
import com.walkingcompiler.data.models.Phone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends MongoRepository<Phone, String> {

    Phone findByCreatePhone(String createPhone);
    Phone findByActivePhone(String activePhone);
    Phone findByPhoneNumber(String phoneNumber);
    Phone findByPhoneId(String phoneId);
    Phone deleteByPhoneNumber(String phoneNumber);
    Phone updateByLocation(String location);
}
