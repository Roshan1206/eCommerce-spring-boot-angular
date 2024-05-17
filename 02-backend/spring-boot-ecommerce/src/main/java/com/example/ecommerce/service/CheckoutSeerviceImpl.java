package com.example.ecommerce.service;

import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.dao.CustomerRepository;
import com.example.ecommerce.dto.Purchase;
import com.example.ecommerce.dto.PurchaseResponse;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;

import jakarta.transaction.Transactional;

@Service
public class CheckoutSeerviceImpl implements CheckoutService {
	
	private CustomerRepository customerRepository;
	
	@Autowired
	public CheckoutSeerviceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {
		// TODO Auto-generated method stub
//		retrieve the order info from dto
		Order order = purchase.getOrder();
		
//		generate the tracking number
		String orderTrackingNumber = generateOrderTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);
		
//		populate order with orderItems
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item -> order.add(item));
		
//		populate order with billingAddress and shippingAddress
		order.setBillingAddress(purchase.getBillingAddress());
		order.setShippingAddress(purchase.getShippingAddress());
		
//		populate customer with order
		Customer customer = purchase.getCustomer();
		
		String theEmail = customer.getEmail();
		
		Customer customerFromDB = customerRepository.findByEmail(theEmail);
		
		if(customerFromDB != null) {
			customer = customerFromDB;
		}
		
		customer.add(order);
		
//		save the order to db
		customerRepository.save(customer);
		
//		return a response
		return new PurchaseResponse(orderTrackingNumber);
	}

	private String generateOrderTrackingNumber() {
//		generate a random UUID
		return UUID.randomUUID().toString();
	}

}
