package edu.chalmers.dat255.audiobookplayer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class FileParser {
	private static final String TAG = "FileParser.java";
	
	public static void writeToInternalStorage(String file, Context c, String content)
			throws IOException {
		
		FileOutputStream outputStream = c.openFileOutput(file, Context.MODE_PRIVATE);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		
		outputStreamWriter.write(content);
	}
	
	public static String readFromInternalStorage(String file, Context c)
			throws FileNotFoundException, IOException{
		FileInputStream inputStream = c.openFileInput(file);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		
		char[] buffer = new char[1024];
		inputStreamReader.read(buffer);
		
		return buffer.toString();
	}
}
