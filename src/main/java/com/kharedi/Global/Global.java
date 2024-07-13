package com.kharedi.Global;

import java.util.ArrayList;
import java.util.List;

import com.kharedi.model.Product;

public class Global {

	
	public static List<Product> cart;
	
	static {
		cart = new ArrayList<Product>();
	}
}
