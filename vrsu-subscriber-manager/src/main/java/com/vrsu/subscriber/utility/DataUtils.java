package com.vrsu.subscriber.utility;

public class DataUtils {

	public static boolean checkDataToSend(String userTopic, String mqttTopic) {
		boolean flag = true;
		if(mqttTopic.contains("REGIONAL/STAT/"))
		{
			return true;
		}
		String[] userArrs = userTopic.split("/");
		String[] mqttArrs = mqttTopic.split("/");
		if (userArrs.length > 0) {
			if (userTopic.contains("#")) {
				for (int i = 0; i < userArrs.length; i++) {
					if (userArrs[i].contains("#")) {
						break;
					}
					if (userArrs[i].equalsIgnoreCase("+") || userArrs[i].equalsIgnoreCase(mqttArrs[i])) {
						continue;
					} else {
						flag = false;
						break;
					}
				}
			} else {
				for (int i = 0; i < userArrs.length; i++) {
					if (userArrs[i].equalsIgnoreCase("+") || userArrs[i].equalsIgnoreCase(mqttArrs[i])) {
						continue;
					} else {
						flag = false;
						break;
					}
				}
			}
			return flag;
		} else {
			return false;
		}
	}
}
