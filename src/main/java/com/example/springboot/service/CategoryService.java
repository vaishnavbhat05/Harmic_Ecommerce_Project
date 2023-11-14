package com.example.springboot.service;

import com.example.springboot.entity.Category;
import com.example.springboot.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

@Autowired
private CategoryRepository categoryRepository;
    public Category addCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }


    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Category updateCategory(Long categoryId, Map<String, Object> updates) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            if (updates.containsKey("catName")) {
                String catName = (String) updates.get("catName");
                category.setCatName(catName);
            }
            return categoryRepository.save(category);
        }
        return null;
    }
}
