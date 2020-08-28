package com.example.emailconfirmation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.emailconfirmation.model.Cart;
import com.example.emailconfirmation.repository.EmailConfirmationRepository;

@RestController
public class EmailConfirmationController {
	
	@Autowired
	EmailConfirmationRepository emailConfirmationRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@PostMapping(value = "/product/confirm" , consumes = MediaType.APPLICATION_JSON_VALUE)
	public String sendEmail(@RequestBody Map<String, String> req) throws Exception{

		System.out.println("inside cart");
		
		if(req.get("price") == null || req.get("cartid") == null || req.get("userid") == null) {
			throw new Exception("input value cannot be null");
		} else if(req.get("price") == "" || req.get("cartid") == "" || req.get("userid") == "") {
			throw new Exception("input value cannot be empty");
		}
		int cartid = Integer.parseInt(req.get("cartid"));
		System.out.println("before template");
		Cart cart = restTemplate.getForObject("http://product-checkout/product/checkout/" + cartid, Cart.class);
		
		double price = Double.parseDouble(req.get("price"));
		if(cart.getPrice() == price) {
			System.out.println("before email");
			SimpleMailMessage msg = new SimpleMailMessage();
			
	        msg.setTo("www.rockshankar007@gmail.com");

	        msg.setSubject("Online Shopping");
	        msg.setText("Your product order confirmed");
	        

	        javaMailSender.send(msg);
	        
	        System.out.println("after email");	        
	        return "Order confirmed.. Check your email";
		} else {
			return "insufficient balance";
		}
		
	}
	
//	@GetMapping("/removeCart/{id}")
//	public String removeCart(@PathVariable int id) throws Exception {		
//		 productCheckoutRepository.deleteByUserId(id);	
//		 return "success";
//	}

}
