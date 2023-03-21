package com.vrsu.subscriber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class InputRequest {
	private String username;
	private String password;
	private String url;
	private String sessionId;
}
