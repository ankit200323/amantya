package com.vrsu.subscriber.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeviceRequest implements Serializable {

	private static final long serialVersionUID = 4097666940424230598L;
	private String ipAddress;
	private String macAddress;
	private String deviceId;
	private ClientInformation clientInformation;
}
