package com.kharedi.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kharedi.Global.Global;
import com.kharedi.model.Product;
import com.kharedi.service.CategoryService;
import com.kharedi.service.ProductService;

@Controller
public class UserController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public String home(Model m) {
		List<Product> featureProducts = productService.getFeatureProducts();
		m.addAttribute("featureProducts", featureProducts);
		m.addAttribute("cartCount" , Global.cart.size());
		return "index";
	}

	@GetMapping("/category/{id}")
	public String showProductByCategoryHome(@PathVariable int id, Model model) {
		model.addAttribute("categories", categoryService.findAllCategory());
		model.addAttribute("products", productService.getAllProductByCategoryId(id));
		return "index";
	}

	@GetMapping("/product")
	public String Products(Model m) {
		List<Product> activeProducts = productService.getActiveProducts();
	    m.addAttribute("products", activeProducts);
		m.addAttribute("categories", categoryService.findAllCategory());
		m.addAttribute("cartCount" , Global.cart.size());
		return "product";
	}

	@GetMapping("/product/category/{id}")
	public String showProductByCategory(@PathVariable int id, Model m) {
		m.addAttribute("categories", categoryService.findAllCategory());
		m.addAttribute("products", productService.getAllProductByCategoryId(id));
		return "product";
	}

	@GetMapping("/product/singleProduct/{id}")
	public String SingleProduct(@PathVariable int id, Model m) {
		m.addAttribute("product", productService.productGetById(id).get());
		m.addAttribute("cartCount" , Global.cart.size());
		return "singleProduct";
	}
	
	

}
