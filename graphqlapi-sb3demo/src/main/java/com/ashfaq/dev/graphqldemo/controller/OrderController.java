package com.ashfaq.dev.graphqldemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashfaq.dev.graphqldemo.model.Order;
import com.ashfaq.dev.graphqldemo.model.User;
import com.ashfaq.dev.graphqldemo.services.OrderService;
import com.ashfaq.dev.graphqldemo.services.UserService;

@RestController
public class OrderController {

	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;

	@MutationMapping
	public Order createOrder(@Argument String orderDetail, @Argument String amount,@Argument("userID") String idStr ) {
		User userById = userService.getUserById(Integer.parseInt(idStr));
		 if (userById == null) {
	            throw new RuntimeException("User not found with ID: " + userById);
	        }
		System.out.println(userById );
		Order order = new Order();
		order.setOrderDetail(orderDetail);
		order.setAmount(Double.parseDouble(amount));
		order.setUser(userById);
		return orderService.createOrder(order);
	}
	
	@QueryMapping(name = "getOrders")
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@QueryMapping(name = "getOrder")
	public Order getOrder(@Argument("orderID") String idStr) {
	    System.out.println("Received ID: " + idStr);
	    int id = Integer.parseInt(idStr); // Convert String to int
	    Order order  = orderService.getOrderById(id);
	    return order;
	}
	
	
	@MutationMapping
	public boolean deleteOrder(@Argument("orderID") String idStr) {
		
		int id = Integer.parseInt(idStr);
		boolean deleteOrder = orderService.deleteOrder(id);
		return deleteOrder;
	}
	
	
	

}
