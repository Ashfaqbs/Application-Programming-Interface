package com.ashfaq.dev.graphqldemo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;

	private String name;

	private String email;

	private String password;

	private String phoneNO;

	// cascade = CascadeType.ALL if user removed respective order removed ,
	// mappedBy = "user" @ManyToOne private User user; in order class that variable
	// we have passed here
	// so it wont create multiple tables just 2 tables and user and order where
	// order table will have userID
// order table orderid, amount, order_detail, userid
//	user table userid, email, name, password, phoneno

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Order> orders = new ArrayList<>();

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNO() {
		return phoneNO;
	}

	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", phoneNO=" + phoneNO + "]";
	}

}
