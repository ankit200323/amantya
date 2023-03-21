package com.vrsu.subscriber.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenericResponse implements Serializable {

	private static final long serialVersionUID = -2991914035432550483L;
	private String message;
	private boolean status;
	private String sessionId;


	public GenericResponse(String message, boolean status) {
		this.message = message;
		this.status = status;
	}

}