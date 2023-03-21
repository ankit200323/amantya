package com.vrsu.subscriber.model;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class MqttConnectionUserTopicData {

	@Getter
	@Setter
	private Set<String> users = new CopyOnWriteArraySet<>();

	@Getter
	@Setter
	private Set<String> topics = new CopyOnWriteArraySet<>();
}
