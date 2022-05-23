package com.dapr.customer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dapr.customer.models.Order;
import com.dapr.customer.models.State;

@Service
public class CustomerService {	
	
	private static final String PUBSUB_NAME = "orderpubsub";
	private static final String TOPIC = "orders";
	private static final String BINDING_NAME= "checkout";
	private static final String STATE_STORE="statestore";
	
	@Value("${DAPR_HOST}")
	private String DAPR_HOST;
	
	@Value("${DAPR_HTTP_PORT}")
	private String DAPR_PORT;
	
	
	private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
	
	public Object getOrderDetails(String orderId) throws Exception {
		var restTemplate = new RestTemplate();
		String url = DAPR_HOST + DAPR_PORT + "/order/" + orderId;	
		
		return restTemplate.exchange(url, HttpMethod.GET,
				createHttpHeaders(), Object.class);
	}

	private HttpEntity<String> createHttpHeaders() {
		var headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("dapr-app-id", "order-service");
		return new HttpEntity<>(headers);

	}

	public void submitPubSubOrder(Order order) throws Exception{
		var restTemplate = new RestTemplate();
		String PUB_SUB_URI = DAPR_HOST + DAPR_PORT + "/v1.0/publish/"+PUBSUB_NAME+"/"+TOPIC;
		restTemplate.exchange(PUB_SUB_URI, HttpMethod.POST,
				createHttpContentForPubSub(order), Object.class);
		LOGGER.info("Order Submitted successfully");
	}
	
	
	private HttpEntity<Order> createHttpContentForPubSub(Order order) {
		var headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");		
		return new HttpEntity<>(order, headers);

	}

	public void submitBindingOrder(Object order) {
		var restTemplate = new RestTemplate();
		String BINDING_URI = DAPR_HOST + DAPR_PORT + "/v1.0/bindings/"+BINDING_NAME;
		restTemplate.exchange(BINDING_URI, HttpMethod.POST,new HttpEntity<>(order), Object.class);
		LOGGER.info("Order Submitted successfully");
		
	}

	public void saveState(State[] state) {
		var restTemplate = new RestTemplate();
		String BINDING_URI = DAPR_HOST + DAPR_PORT + "/v1.0/state/"+STATE_STORE;
		var headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");	
		restTemplate.exchange(BINDING_URI, HttpMethod.POST,new HttpEntity<>(state,headers), Object.class);
		LOGGER.info("State Saved successfully");
		
	}

	public String getKeyFromStateStore(String key) {
		var restTemplate = new RestTemplate();
		String STATE_STORE_URI = DAPR_HOST + DAPR_PORT + "/v1.0/state/" + STATE_STORE + "/" + key;
		ResponseEntity<String> ret = restTemplate.exchange(STATE_STORE_URI, HttpMethod.GET, null, String.class);
		LOGGER.info("State Store Key Retrieved: " + ret.getBody());
		return ret.getBody();
	}

	public Object verifyMiddlewareExecution(String body) {		
		var restTemplate = new RestTemplate();
		String url = DAPR_HOST + DAPR_PORT + "/order/middleware" ;	
		var headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");		
		headers.set("dapr-app-id", "order-service");
		return restTemplate.exchange(url, HttpMethod.POST,
				new HttpEntity<>(body, headers), String.class);
	}
	
}
