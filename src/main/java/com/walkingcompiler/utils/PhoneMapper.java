package com.walkingcompiler.utils;
import com.walkingcompiler.dto.PhoneDto;
import com.walkingcompiler.data.models.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhoneMapper {

    public Phone mapToPhoneDto(PhoneDto phone) {
        return new Phone(
                phone.getId(),
                phone.getPhoneNumber(),
                phone.getBrand(),
                phone.getModel(),
                phone.getLatitude(),
                phone.getLongitude(),
                phone.getLastSeen(),
                phone.getIsActive()
        );
    }
    public PhoneDto mapToPhone(Phone phoneDto) {
        return new PhoneDto(
                phoneDto.getId(),
                phoneDto.getPhoneNumber(),
                phoneDto.getBrand(),
                phoneDto.getModel(),
                phoneDto.getLatitude(),
                phoneDto.getLongitude(),
                phoneDto.getLastSeen(),
                phoneDto.getIsActive()
        );
    }
}
