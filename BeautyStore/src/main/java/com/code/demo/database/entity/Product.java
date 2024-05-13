package com.code.demo.database.entity;


import java.util.Date;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name="products")
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	private String name;
	private String brand;
	private String Category;
	private double price;
	
	 @Column(columnDefinition = "TEXT")
	private String  description;
    private Date  createdAt;
	private String imageFileName;

}
