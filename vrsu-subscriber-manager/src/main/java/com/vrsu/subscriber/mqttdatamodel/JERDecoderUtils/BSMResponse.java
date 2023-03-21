package com.vrsu.subscriber.mqttdatamodel.JERDecoderUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class BSMResponse {

    @SerializedName("MessageFrame")
    public MessageFrame messageFrame = new MessageFrame();
    
    public String type;
    public String subtype;
    public String topicType;

	public class Value {
        @SerializedName("BasicSafetyMessage")
        public BasicSafetyMessage basicSafetyMessage = new BasicSafetyMessage();
    }

    public class BasicSafetyMessage {
        public BasicCoreData coreData = new BasicCoreData();
    }

    public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getSubtype() {
		return subtype;
	}


	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	
    public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}


	public class AccelSet {
        @SerializedName("long")
        public String mylong;
        public String lat;
        public String vert;
        public String yaw;
    }

    public class Accuracy {
        public String semiMajor;
        public String semiMinor;
        public String orientation;
    }

    public class Brakes {
        public String wheelBrakes;
        public Map<AbsValues, String> traction;
        public Map<AbsValues, String> abs;
        public Map<AbsValues, String> scs;
        public Map<AbsValues, String> brakeBoost;
        public Map<AbsValues, String> auxBrakes;
    }

    public enum AbsValues {
        unavailable,
        off,
        on,
        engaged
    }

    public class BasicCoreData {
        public String id;
        public Map<TransmissionValues, String> transmission;
        public Size size = new Size();
        public String lat;
        public String elev;
        public String speed;
        public String secMark;
        @SerializedName("long")
        public String mylong;
        public AccelSet accelSet = new AccelSet();
        public String msgCnt;
        public Accuracy accuracy = new Accuracy();
        public String heading;
        public String angle;
        public Brakes brakes = new Brakes();
    }

    public enum TransmissionValues {
        neutral,
        park,
        forwardGears,
        reverseGears,
        reserved1,
        reserved2,
        reserved3,
        unavailable

    }

    public class MessageFrame {
        public String messageId;
        public Value value = new Value();
    }


    public class Size {
        public String width;
        public String length;
    }
}


