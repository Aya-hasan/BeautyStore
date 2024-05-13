package com.code.demo.controller;

import java.io.InputStream;
import java.nio.file.*;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.multipart.MultipartFile;

import com.code.demo.database.entity.Product;
import com.code.demo.database.entity.ProductDto;
import com.code.demo.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/products")
public class ProductController {
@Autowired
private ProductService productService;

@GetMapping({"","/"})
public String showProducts(Model model) {	
List<Product>products=productService.getAllProducts();
model.addAttribute("products",products);
return "products/index";	
}
 @GetMapping("/create")
 public String showCreatePage(Model model) {
	 ProductDto productDto=new ProductDto();
	 model.addAttribute("productDto",productDto);
	 return "products/CreateProduct";	
	 }


@PostMapping("/create")
public String  createProduct(@Valid @ModelAttribute ProductDto productDto,BindingResult result) {
if(productDto.getImageFile().isEmpty()) {
	result.addError(new FieldError("productDto","imageFile","The Image File Is Required"));
}
if(result.hasErrors()) {
	 return "products/CreateProduct";
}
//Save image file
MultipartFile image =productDto.getImageFile();
Date createdAt=new Date();
String FileName=createdAt.getTime()+"_"+image.getOriginalFilename();
try {
	String uploadDirector="public/images/";
	Path uploadPath=Paths.get(uploadDirector);
	if(!Files.exists(uploadPath)) {
		Files.createDirectories(uploadPath);
	}
try (InputStream inputStream=image.getInputStream()){
	Files.copy(inputStream, Paths.get(uploadDirector+FileName),StandardCopyOption.REPLACE_EXISTING);
}
}

catch(Exception ex){
	System.out.println("Exeption"+ ex.getMessage());
}
//create product object to save data comes from form at dto type (to save in data base turn it into entity)
Product product=new Product();
product.setName(productDto.getName());
product.setBrand(productDto.getBrand());
product.setDescription(productDto.getDescription());
product.setPrice(productDto.getPrice());
product.setCategory(productDto.getCategory());
product.setCreatedAt(createdAt);
product.setImageFileName(FileName);
productService.addProduct(product);
    return "redirect:/products";
}



@GetMapping("/edit/{id}")
public String showEditPage(Model model,@PathVariable("id") int id) {
  try {
	  Product product =productService.getProduct(id);
	  model.addAttribute("product",product);
	  ProductDto productDto=new ProductDto();
	  productDto.setName(product.getName());
	  productDto.setBrand(product.getBrand());
	  productDto.setCategory(product.getCategory());
	  productDto.setDescription(product.getDescription());
	  productDto.setPrice(product.getPrice());
	  model.addAttribute("productDto",productDto);
  }
  catch(Exception ex) {
	  System.out.println("Exeption:"+ex.getMessage());
	  return "redirect:/products";
  }
	
    return "products/EditProduct";
}
@PostMapping("/edit/{id}")
public String updateProduct(
		Model model,
		@PathVariable("id") int id,
		
		@Valid @ModelAttribute ProductDto productDto,BindingResult result)
{
	try {
		  Product product =productService.getProduct(id);
		  model.addAttribute("product",product);
		  if(result.hasErrors()) {return  "products/EditProduct";}
		  if(!productDto.getImageFile().isEmpty()) {
		 //delete old image 
			  String uploadDir ="public/images/";
			  Path oldImagePath=Paths.get(uploadDir+product.getImageFileName());
			 try { Files.delete(oldImagePath);}
			 catch (Exception ex) {
				  System.out.println("Exeption:"+ex.getMessage());
			}
	     //Save new image
			 MultipartFile image= productDto.getImageFile();
			 Date createdAt=new Date();
			 String newImgFileName=createdAt.getTime()+"_"+image.getOriginalFilename();
			 try(InputStream inputStream=image.getInputStream()){
				 Files.copy(inputStream, Paths.get(uploadDir+newImgFileName), StandardCopyOption.REPLACE_EXISTING);
			 }
			 product.setImageFileName(newImgFileName);
		  }
		  
		  product.setName(productDto.getName());
		  product.setBrand(productDto.getBrand());
		  product.setCategory(productDto.getCategory());
		  product.setDescription(productDto.getDescription());
		  product.setPrice(productDto.getPrice());
		  productService.addProduct(product);
		
	}
	catch(Exception ex) {
		  System.out.println("Exeption:"+ex.getMessage());
		
	  }
	 return "redirect:/products";}


@GetMapping("/delete/{id}")
public String deleteProduct(@PathVariable("id") Integer id) {
	try {
		Product product =productService.getProduct(id);
		//delete product image 
		Path imagePath=Paths.get("public/images/"+product.getImageFileName());
		try {
			Files.delete(imagePath);
		}
		catch (Exception ex) {
			  System.out.println("Exeption:"+ex.getMessage());
		}	
		
		productService.deleteProduct(product);
	}
	catch (Exception ex) {
		  System.out.println("Exeption:"+ex.getMessage());
	}
	
	
	 return "redirect:/products";
}


}
