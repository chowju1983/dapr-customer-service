package com.dapr.customer.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class State implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String value;
}
