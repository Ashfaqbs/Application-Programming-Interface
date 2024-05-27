package com.ashfaq.dev.graphqldemo.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ashfaq.dev.graphqldemo.dao.OrderDao;
import com.ashfaq.dev.graphqldemo.helper.ExceptionHelper;
import com.ashfaq.dev.graphqldemo.model.Order;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;

	// Create a new order
	public Order createOrder(Order order) {
		return orderDao.save(order);
	}

	// Get all orders
	public List<Order> getAllOrders() {
		return orderDao.findAll();
	}

	// Get order by ID
	public Order getOrderById(int id) {
		return orderDao.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
	}

	// Update an order
	public Order updateOrder(int id, Order orderDetails) {
		Order order = orderDao.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
		order.setOrderDetail(orderDetails.getOrderDetail());
		order.setAmount(orderDetails.getAmount());
		return orderDao.save(order);
	}

	// Delete an order
	public boolean deleteOrder(int id) {
		Order order = orderDao.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
		orderDao.delete(order);
		return true;
	}
}
