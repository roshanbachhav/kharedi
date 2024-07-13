package com.kharedi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kharedi.model.OrderedData;
import com.kharedi.repository.OrderRepository;

@Service
public class OrderDataService {
	@Autowired
	private OrderRepository orderRepository;

	public void saveOrder(OrderedData order) {
		orderRepository.save(order);
	}

	public List<OrderedData> getAllOrders() {
		return orderRepository.findAll();
	}

	public OrderedData findById(Long orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}
	
	public void deleteOrderById(Long id) {
		orderRepository.deleteById(id);
	}
}
