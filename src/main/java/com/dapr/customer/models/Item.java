package com.dapr.customer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
	private String itemName;
	private String itemDescription;
	private int count;

}
