package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    private List<Category> categories = new ArrayList<>(Arrays.asList(
        new Category(1, "Điện thoại"),
        new Category(2, "Laptop"),
        new Category(3, "Máy tính bảng"),
        new Category(4, "Phụ kiện")
    ));
    
    public List<Category> getAllCategories() {
        return categories;
    }
    
    public Category getCategoryById(int id) {
        return categories.stream()
                .filter(category -> category.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public void addCategory(Category category) {
        int newId = categories.stream()
                .mapToInt(Category::getId)
                .max()
                .orElse(0) + 1;
        category.setId(newId);
        categories.add(category);
    }
    
    public void updateCategory(int id, Category updatedCategory) {
        categories.stream()
                .filter(category -> category.getId() == id)
                .findFirst()
                .ifPresent(category -> {
                    category.setName(updatedCategory.getName());
                });
    }
    
    public void deleteCategory(int id) {
        categories.removeIf(category -> category.getId() == id);
    }
}
