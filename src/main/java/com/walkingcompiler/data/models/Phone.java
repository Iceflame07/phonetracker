package com.walkingcompiler.data.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "phones")
@Data
@Getter
@Setter
@AllArgsConstructor
public class Phone {
    
    @Id
    private String id;

    private String phoneNumber;
    private String brand;
    private String model;
    private Double latitude;
    private Double longitude;
    private LocalDateTime lastSeen;
    private Boolean isActive;
}
