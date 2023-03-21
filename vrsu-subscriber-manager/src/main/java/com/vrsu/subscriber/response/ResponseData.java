package com.vrsu.subscriber.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String message;
	private boolean status; 
}
