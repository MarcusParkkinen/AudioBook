package edu.chalmers.dat255.audiobookplayer.util;

import java.io.FileNotFoundException;

import com.google.gson.Gson;

public class JSONParser {
	public static <T> T fromJSON(String JSONString, Class<T> type)
			throws FileNotFoundException {
		
		return (new Gson()).fromJson(JSONString, type);
	}
	
	public static <T> String toJSON(Object obj) {
		return (new Gson()).toJson(obj);
	}
}
