package com.vrsu.subscriber.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WebSocketDataReceiever {

	private String topicName;
	private boolean isSubscribe;
	private String filterName;
	private String sessionId;
}