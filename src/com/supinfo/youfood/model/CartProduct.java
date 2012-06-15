package com.supinfo.youfood.model;

public class CartProduct {
	private int quantity = 1;
	private Product product;
	
	public CartProduct(Product product) {
		this.product = product;
	}
	public CartProduct(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void addQuantity() {
		this.quantity++;
	}
	public void removeQuantity() {
		this.quantity--;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
