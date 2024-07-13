package com.kharedi.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kharedi.model.Product;


public interface ProductRepository extends JpaRepository<Product , Long> {
	List<Product> findAllByCategory_Id(int id);
	
	List<Product> findByActiveTrue();

	List<Product> findByFeatureProducts(boolean featureProducts);

}
