package com.dapr.customer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dapr.customer.models.Order;
import com.dapr.customer.models.State;
import com.dapr.customer.services.CustomerService;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customer/order/{orderId}")
	public Object getOrderDetails(@PathVariable("orderId")String orderId) throws Exception {
		return customerService.getOrderDetails(orderId);
	}
	
	@PostMapping(value = "/customer/pubSub/order")
	public Object postPubSubOrder(@RequestBody Order order) throws Exception{
		customerService.submitPubSubOrder(order);
		return new ResponseEntity<String>("Pub Sub Order Posted Successfully", HttpStatus.OK);
	}

	
	@PostMapping(value = "/customer/binding/order")
	public Object postBindingOrder(@RequestBody Object order) throws Exception{
		customerService.submitBindingOrder(order);
		return new ResponseEntity<String>("Binding Order Posted Successfully", HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/customer/state/save")
	public Object saveState(@RequestBody List<State>  state) throws Exception{
		
		customerService.saveState(state.toArray(new State[0]));
		return new ResponseEntity<String>("State Saved Successfully", HttpStatus.OK);
	}
	
	@GetMapping(path = "/customer/state/{key}")
	public ResponseEntity<?> getCustomerKeyValue(@PathVariable("key") String key) {		
		return new ResponseEntity<>(customerService.getKeyFromStateStore(key), HttpStatus.OK);
		
	}
}
