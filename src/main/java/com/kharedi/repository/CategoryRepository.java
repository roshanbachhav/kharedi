package com.kharedi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kharedi.model.Category;


public interface CategoryRepository extends JpaRepository<Category , Integer>{
    
}
