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
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initializeSampleData() throws IOException {
        if (categoryRepository.count() == 0) {
            ClassPathResource resource = new ClassPathResource("data/categories.json");
            List<Category> sampleCategories = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<Category>>() {});
            categoryRepository.saveAll(sampleCategories);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}