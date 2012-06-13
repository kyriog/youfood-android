package com.supinfo.youfood.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 106010792290687806L;
	
	private int id;
	private String name;
	private ArrayList<Product> products = new ArrayList<Product>();
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public Product getProduct(int position) {
		return products.get(position);
	}
	public void addProduct(Product p) {
		products.add(p);
	}
}
