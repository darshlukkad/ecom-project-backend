package com.project.ecom_proj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.project.ecom_proj.model.Product;
import com.project.ecom_proj.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
	
	
	private ProductService service;
	
	@Autowired
	public ProductController(ProductService serv) {
		this.service=serv;
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		return new ResponseEntity<List<Product>>(service.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable String id) {
		
		Product product =service.getProduct(Integer.parseInt(id));
		
		if(product.getId()==0)
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile ) {
		Product p;
		try {
			p=service.addProduct(product,imageFile);
			if(p==null)
				return new ResponseEntity<>(HttpStatus.METHOD_FAILURE);
			return new ResponseEntity<>(p,HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.METHOD_FAILURE);
		}
	}
	
	@GetMapping("/product/{id}/image")
	public ResponseEntity<byte[]> getImage( @PathVariable int id) {
		Product product = service.getProduct(id);
		byte[] imageFile=product.getImageDate();
		
		return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<?> addProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile ) {
		Product p;
		try {
			p=service.getProduct(id);
			p=service.updateProduct(product,imageFile);
			
			return new ResponseEntity<>(p,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.METHOD_FAILURE);
		}
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id) {
		service.deleteProduct(id);
		return new ResponseEntity<>("product deleted",HttpStatus.OK);
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
		System.out.println("keyword:"+keyword);
		List<Product> products = service.searchProducts(keyword);
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	}
}
