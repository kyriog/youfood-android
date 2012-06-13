package com.supinfo.youfood.activity;

import com.supinfo.youfood.adapter.CategoryAdapter;
import com.supinfo.youfood.model.Category;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CategoryActivity extends Activity {
	private Category category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		Bundle extras = getIntent().getExtras();
		category = (Category) extras.getSerializable("category");
		
		CategoryAdapter adapter = new CategoryAdapter(this, category.getProducts());
		ListView products = (ListView) findViewById(R.id.products_list);
		products.setAdapter(adapter);
	}
}
