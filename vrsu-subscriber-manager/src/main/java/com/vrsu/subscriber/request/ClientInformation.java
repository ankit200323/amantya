package com.vrsu.subscriber.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
public class ClientInformation implements Serializable {

	private static final long serialVersionUID = -5721653517967737402L;

	private String entityType;
	private String entitySubtype;
	private String vendorId;
}
