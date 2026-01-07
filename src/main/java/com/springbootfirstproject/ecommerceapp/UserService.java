package com.springbootfirstproject.ecommerceapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException {
        if (userRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("data/users.json");
            List<User> sampleUsers = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<User>>() {});
            userRepository.saveAll(sampleUsers);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}