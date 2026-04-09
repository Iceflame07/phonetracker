package com.walkingcompiler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkingcompiler.dto.PhoneDto;
import com.walkingcompiler.data.repository.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestMvc
@ActiveProfiles("test")
class PhoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PhoneRepository phoneRepository;

    private PhoneDto testPhoneDto;

    @BeforeEach
    void setUp() {
        phoneRepository.deleteAll();
        testPhoneDto = new PhoneDto("1234567890", "Apple", "iPhone 14", 40.7128, -74.0060);
    }

    @Test
    void shouldCreatePhoneSuccessfully() throws Exception {
        mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value(testPhoneDto.getPhoneNumber()))
                .andExpect(jsonPath("$.brand").value(testPhoneDto.getBrand()))
                .andExpect(jsonPath("$.model").value(testPhoneDto.getModel()))
                .andExpect(jsonPath("$.latitude").value(testPhoneDto.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(testPhoneDto.getLongitude()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.lastSeen").exists());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingPhoneWithInvalidData() throws Exception {
        PhoneDto invalidPhone = new PhoneDto("", "", "", null, null);
        
        mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPhone)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetPhoneByIdSuccessfully() throws Exception {
        String response = mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        PhoneDto createdPhone = objectMapper.readValue(response, PhoneDto.class);
        
        mockMvc.perform(post("/api/phones/{id}", createdPhone.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdPhone.getId()))
                .andExpect(jsonPath("$.phoneNumber").value(testPhoneDto.getPhoneNumber()));
    }

    @Test
    void shouldReturnNotFoundWhenGettingNonExistentPhone() throws Exception {
        mockMvc.perform(post("/api/phones/{id}", "nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetPhoneByNumberSuccessfully() throws Exception {
        mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated());
        
        mockMvc.perform(post("/api/phones/number/{phoneNumber}", testPhoneDto.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value(testPhoneDto.getPhoneNumber()));
    }

    @Test
    void shouldUpdatePhoneSuccessfully() throws Exception {
        String response = mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        PhoneDto createdPhone = objectMapper.readValue(response, PhoneDto.class);
        createdPhone.setBrand("Samsung");
        createdPhone.setModel("Galaxy S23");
        
        mockMvc.perform(post("/api/phones/update/{id}", createdPhone.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdPhone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Samsung"))
                .andExpect(jsonPath("$.model").value("Galaxy S23"));
    }

    @Test
    void shouldDeletePhoneSuccessfully() throws Exception {
        String response = mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        PhoneDto createdPhone = objectMapper.readValue(response, PhoneDto.class);
        
        mockMvc.perform(post("/api/phones/delete/{id}", createdPhone.getId()))
                .andExpect(status().isNoContent());
        
        mockMvc.perform(post("/api/phones/{id}", createdPhone.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateLocationSuccessfully() throws Exception {
        String response = mockMvc.perform(post("/api/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPhoneDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        PhoneDto createdPhone = objectMapper.readValue(response, PhoneDto.class);
        
        mockMvc.perform(post("/api/phones/{id}/location", createdPhone.getId())
                .param("latitude", "51.5074")
                .param("longitude", "-0.1278"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(51.5074))
                .andExpect(jsonPath("$.longitude").value(-0.1278));
    }
}
