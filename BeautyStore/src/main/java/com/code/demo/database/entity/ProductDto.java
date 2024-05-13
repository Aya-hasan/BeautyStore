package com.code.demo.database.entity;



import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class ProductDto {
	@NotEmpty(message = "The name is required")
	private String name;
	@NotEmpty(message = "The brand is required")
	private String brand;
	@NotEmpty(message = "The Category is required")
	private String Category;
	@Min(0)
	private double price;
	
	 @Size(min = 10,message = "The description should be at least 10 characters")
	 @Size(max = 2000,message = "The description should not be more than 2000 characters" )
	private String  description;
  
	private MultipartFile imageFile;


}
