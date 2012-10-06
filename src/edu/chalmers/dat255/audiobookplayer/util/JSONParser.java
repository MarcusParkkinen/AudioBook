package edu.chalmers.dat255.audiobookplayer.util;

import com.google.gson.Gson;

public class JSONParser {
	public static <T> T fromJSON(String JSONString, Class<T> type) {
		return (new Gson()).fromJson(JSONString, type);
	}
	
	public static String toJSON(Object obj) {
		return (new Gson()).toJson(obj);
	}
}
