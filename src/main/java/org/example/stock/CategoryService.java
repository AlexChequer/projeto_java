package org.example.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category createCategory(Category category) {
        Category existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category categoryDetails) {
        Category category = getCategoryById(id);
        
        if (categoryDetails.getName() != null) {
            Category existingCategory = categoryRepository.findByName(categoryDetails.getName());
            if (existingCategory != null && existingCategory.getId() != id) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
            }
            category.setName(categoryDetails.getName());
        }
        
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}