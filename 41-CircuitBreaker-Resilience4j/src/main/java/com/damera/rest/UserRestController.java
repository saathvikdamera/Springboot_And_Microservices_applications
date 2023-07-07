package com.damera.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.damera.dto.OrderDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/user-service")
public class UserRestController {

	 @Autowired
	    @Lazy
	    private RestTemplate restTemplate;

	    public static final String USER_SERVICE="userService";

	    private static final String BASEURL = "http://localhost:8080/orders";

	    private int attempt=1;


	    @GetMapping("/displayOrders")
	    @CircuitBreaker(name =USER_SERVICE,fallbackMethod = "getAllAvailableProducts")
	   // @Retry(name = USER_SERVICE,fallbackMethod = "getAllAvailableProducts")
	    public List<OrderDTO> displayOrders(@RequestParam("category") String category) {
	        String url = category == null ? BASEURL : BASEURL + "/" + category;
	        System.out.println("retry method called "+attempt++ +" times "+" at "+new Date());
	        return restTemplate.getForObject(url, ArrayList.class);
	    }


	    public List<OrderDTO> getAllAvailableProducts(Exception e){
	        return Stream.of(
	                new OrderDTO(119, "LED TV", "electronics", "white", 45000.0),
	                new OrderDTO(345, "Headset", "electronics", "black", 7000.0),
	                new OrderDTO(475, "Sound bar", "electronics", "black", 13000.0),
	                new OrderDTO(574, "Puma Shoes", "foot wear", "black & white", 4600.0),
	                new OrderDTO(678, "Vegetable chopper", "kitchen", "blue", 999.0),
	                new OrderDTO(532, "Oven Gloves", "kitchen", "gray", 745.0)
	        ).collect(Collectors.toList());
	    }

}