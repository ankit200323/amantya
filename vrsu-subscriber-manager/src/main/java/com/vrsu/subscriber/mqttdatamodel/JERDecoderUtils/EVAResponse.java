package com.vrsu.subscriber.mqttdatamodel.JERDecoderUtils;

import com.google.gson.annotations.SerializedName;

public class EVAResponse {

    @SerializedName("MessageFrame")
    public MessageFrame messageFrame = new MessageFrame();

    public String type;
    public String topicType;

	public class MessageFrame {
        public String messageId;
        public Value value = new Value();
    }

    public class Value {
        @SerializedName("EmergencyVehicleAlert")
        public EmergencyVehicleAlert emergencyVehicleAlert = new EmergencyVehicleAlert();
    }

    public class EmergencyVehicleAlert {
        public RSAResponse.RoadSideAlert rsaMsg;
        public String id;
        public String timeStamp;

    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}
    
    
}
