package com.supinfo.youfood.adapter;

import java.util.ArrayList;

import com.supinfo.youfood.listener.ChangeQuantityListener;
import com.supinfo.youfood.listener.ConfirmDeleteListener;
import com.supinfo.youfood.model.CartProduct;
import com.supinfo.youfood.model.Product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RightCartAdapter extends BaseAdapter {
	private ArrayList<CartProduct> cartProducts = new ArrayList<CartProduct>();
	
	private Context context;
	private Button checkoutButton;
	private TextView total;
	
	public RightCartAdapter(Context c, Button cB, TextView t) {
		context = c;
		checkoutButton = cB;
		total = t;
		setCheckoutButtonStatus();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		setCheckoutButtonStatus();
		total.setText(getGlobalPrice() + " €");
	}

	public int getCount() {
		return cartProducts.size();
	}

	public CartProduct getItem(int location) {
		return cartProducts.get(location);
	}

	public long getItemId(int location) {
		return location;
	}

	public View getView(int location, View convertView, ViewGroup parent) {
		CartProduct cartProduct = getItem(location);
		
		LinearLayout view = new LinearLayout(context);
		
		TextView quantity = new TextView(context);
		
		LinearLayout itemLayout = new LinearLayout(context);
		TextView name = new TextView(context);
		
		LinearLayout buttonsLayout = new LinearLayout(context);
		Button removeQuantity = new Button(context);
		Button addQuantity = new Button(context);
		
		ChangeQuantityListener listener = new ChangeQuantityListener(ChangeQuantityListener.TYPE_REMOVE, cartProduct, this);
		removeQuantity.setText("-");
		removeQuantity.setOnClickListener(listener);
		listener = new ChangeQuantityListener(ChangeQuantityListener.TYPE_ADD, cartProduct, this);
		addQuantity.setText("+");
		addQuantity.setOnClickListener(listener);
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonsLayout.setGravity(Gravity.RIGHT);
		buttonsLayout.addView(removeQuantity);
		buttonsLayout.addView(addQuantity);
		
		name.setText(cartProduct.getProduct().getName() + " — " + (cartProduct.getProduct().getPrice() * cartProduct.getQuantity()) + " €");
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		itemLayout.addView(name);
		itemLayout.addView(buttonsLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
		
		quantity.setTextSize(50);
		quantity.setText(String.valueOf(cartProduct.getQuantity()));
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.addView(quantity);
		view.addView(itemLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		return view;
	}
	
	public CartProduct getCartProductFor(Product product) {
		for(CartProduct cartProduct : cartProducts) {
			if(product.equals(cartProduct.getProduct()))
				return cartProduct;
		}
		return null;
	}
	
	public void addProductToCart(Product product) {
		cartProducts.add(new CartProduct(product));
		notifyDataSetChanged();
	}
	
	public void verifyQuantityFor(CartProduct cartProduct) {
		if(cartProduct.getQuantity() == 0) {
			ConfirmDeleteListener.setAdapter(this);
			ConfirmDeleteListener listener = new ConfirmDeleteListener(cartProduct);
			
			AlertDialog confirm = new AlertDialog.Builder(context).create();
			confirm.setTitle("Êtes-vous sûr ?");
			confirm.setMessage("Voulez-vous vraiment supprimer " + cartProduct.getProduct().getName() + " de votre panier ?");
			confirm.setButton(DialogInterface.BUTTON_POSITIVE, "Oui, je veux supprimer ce produit", listener);
			confirm.setButton(DialogInterface.BUTTON_NEGATIVE, "Non, garder ce produit dans mon panier", listener);
			confirm.setOnCancelListener(listener);
			
			confirm.show();
		}
	}
	
	public void removeFromCart(CartProduct cartProduct) {
		cartProducts.remove(cartProduct);
		notifyDataSetChanged();
	}
	
	public int getGlobalQuantity() {
		int quantity = 0;
		for(CartProduct cartProduct : cartProducts) {
			quantity += cartProduct.getQuantity();
		}
		return quantity;
	}
	
	public float getGlobalPrice() {
		float price = 0;
		for(CartProduct cartProduct : cartProducts) {
			price += cartProduct.getQuantity() * cartProduct.getProduct().getPrice();
		}
		return price;
	}
	
	private void setCheckoutButtonStatus() {
		checkoutButton.setEnabled(getCount() > 0);
	}
	
	public ArrayList<CartProduct> getCartProducts() {
		return cartProducts;
	}
	
	public void clearCart() {
		cartProducts.clear();
		notifyDataSetChanged();
	}
}
