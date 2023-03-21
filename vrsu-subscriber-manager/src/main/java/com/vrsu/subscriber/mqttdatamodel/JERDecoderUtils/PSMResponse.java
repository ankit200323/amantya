package com.vrsu.subscriber.mqttdatamodel.JERDecoderUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PSMResponse {

    @SerializedName("MessageFrame")
    public MessageFrame messageFrame=new MessageFrame();

    public String type;
    public String topicType;

	public class Value {
        @SerializedName("PersonalSafetyMessage")
        public PersonalSafetyMessage personalSafetyMessage = new PersonalSafetyMessage();
    }

    public class Accuracy {
        public String semiMajor="";
        public String semiMinor="";
        public String orientation="";
    }

    public enum BasicTypevalues {
        aPEDESTRIAN,
        unavailable,
        aPEDALCYCLIST,
        aPUBLICSAFETYWORKER,
        anANIMAL
    }

    public class MessageFrame {
        public String messageId;
        public Value value = new Value();
    }

    public class PersonalSafetyMessage {
        public Accuracy accuracy = new Accuracy();
        public String speed;
        public String heading;
        //public Map<BasicTypevalues, String> basicType;
        public String secMark;
        public String msgCnt;
        public String id;
        public Position position = new Position();
    }

    public class Position {
        public String lat;
        @SerializedName("long")
        public String mylong;
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
