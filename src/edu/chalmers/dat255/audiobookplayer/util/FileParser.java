package edu.chalmers.dat255.audiobookplayer.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		dataOutputStream.writeUTF(content);
		dataOutputStream.close();
	}
	
	public static String readFromInternalStorage(String file, Context c)
			throws FileNotFoundException, IOException{
		FileInputStream inputStream = c.openFileInput(file);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		
		String result = dataInputStream.readUTF();
		dataInputStream.close();
		return result;
	}
}
