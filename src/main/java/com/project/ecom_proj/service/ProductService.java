package com.project.ecom_proj.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.project.ecom_proj.model.Product;
import com.project.ecom_proj.repo.ProductRepo;

@Component
public class ProductService{
	
	ProductRepo repo;
	
	@Autowired
	public ProductService(ProductRepo r, JdbcTemplate j) {
		this.repo=r;
	}
	
	public List<Product> getAllProducts() {
		return repo.findAll();
	}
	
	public Product getProduct(int id) {
		return repo.findById(id).orElse(new Product());
	}
	
	public Product addProduct(Product prod, MultipartFile mpf) {
		try {
			prod.setImageName(mpf.getOriginalFilename());
			prod.setImageType(mpf.getContentType());
			prod.setImageDate(mpf.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
		return repo.save(prod);
	}

	public byte[] getImage(int id) {
		return getProduct(id).getImageDate();
		
	}

	public Product updateProduct(Product product, MultipartFile imageFile) throws IOException {
		product.setImageDate(imageFile.getBytes());
		product.setImageName(imageFile.getOriginalFilename());
		product.setImageType(imageFile.getContentType());
		return repo.save(product);
	}

	public void deleteProduct(int id) {
		
		repo.deleteById(id);
	}

	public List<Product> searchProducts(String keyword) {
		return repo.searchProducts(keyword);
		
	}
}
