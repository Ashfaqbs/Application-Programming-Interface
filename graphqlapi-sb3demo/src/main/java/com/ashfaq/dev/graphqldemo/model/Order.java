package com.ashfaq.dev.graphqldemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order {

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderID;

	private String orderDetail;

	private double amount;

//	if added //	@JoinColumn(name = "userID") //order table will have like amount, orderid, user_userid, order_detail
	
	@ManyToOne
	@JoinColumn(name = "userID") 
	private User user;

	public int getOrderID() {
		return orderID;
	} 

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", orderDetail=" + orderDetail + ", amount=" + amount + "]";
	}

}
