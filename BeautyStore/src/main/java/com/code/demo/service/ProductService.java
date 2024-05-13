package com.code.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.demo.database.entity.Product;
import com.code.demo.database.repository.ProductRepository;


@Service
public class ProductService {
@Autowired
private  ProductRepository productRepo;

public Product addProduct(Product product) {
	
	return productRepo.save(product);
}

public Product updateProduct(Product product) {

	return productRepo.save(product);
}

public void deleteProduct(Product product) {

	productRepo.delete(product);
}

public List<Product> getAllProducts() {
	
	return productRepo.findAll();
}

public Product getProduct(Integer id) {
	Optional<Product> Product=this.productRepo.findById(id);
	if( Product.isPresent()) {return  Product.get() ;}
	
	return null;
}
	

	

}
