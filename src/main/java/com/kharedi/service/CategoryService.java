package com.kharedi.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kharedi.model.Category;
import com.kharedi.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> findAllCategory(){
        return categoryRepository.findAll();
    }

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public void deleteCategoryById(int id){
        categoryRepository.deleteById(id);
    }

    public Optional<Category> putCategoryById(int id){
        return categoryRepository.findById(id);
    }
    
    

}
