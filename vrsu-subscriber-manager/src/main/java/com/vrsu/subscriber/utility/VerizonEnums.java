package com.vrsu.subscriber.utility;

public enum VerizonEnums {

	REGIONALDYN3("REGIONAL/DYN/3"),
	UPERBSM("UPER/BSM"),
	UPEREVA("UPER/EVA"),
	JEREVA("JER/EVA"),
	JERBSM("JER/BSM");

	private final String name;

	VerizonEnums(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.name;
	}
}
