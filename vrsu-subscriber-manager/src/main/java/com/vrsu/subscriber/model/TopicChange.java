package com.vrsu.subscriber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TopicChange {

	private String topicName;
	private String sessionId;
	private String filterName;
}
