package com.kharedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kharedi.model.OrderedData;

@Repository
public interface OrderRepository extends JpaRepository<OrderedData, Long> {
	
}
