package edu.chalmers.dat255.audiobookplayer.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 * Class containing static methods to help create a file browser.
 * @author Fredrik Åhs
 *
 */
public  class FileBrowser {
/*	Regexp					Explanation
 	(.*\\.)(mp3|wav)$		Searchpattern used to identify filename extensions, in this case mp3 or wav.
 						
 	.* 						An arbitrary number of arbitrary characters
	\\. 					followed by a dot, which then is
 	(format1|format2|...) 	followed by one of these formats.
 	$ 						Indicates the end of the line.
*/
	private final static Pattern PATTERN = Pattern.compile("(.*\\.)(mp3|wav)$"); 
	/**
	 * Searches the directory (file) located at path, and its subdirectories (files), for files matching set pattern.
	 * @param root The path to the root of the files being searched.
	 * @return A file tree starting at root containing all matched files (according to pattern) and their parent directories.
	 */
	public static List<File> getFileTree(String root) { //wrapper
		return getFileTree(new File(root));
	}
	
	/**
	 * Searches a directory (file) and its subdirectories (files) recursively for files matching set pattern.
	 * @param file The root of the files being searched.
	 * @return A file tree starting at root containing all matched files (according to pattern) and their parent directories.
	 */
	public static List<File> getFileTree(File file) {
		ArrayList<File> list = new ArrayList<File>();
		if(file.isDirectory()) {
			for (File f : file.listFiles()) { 	//search files inside file and its subdirectories
				list.addAll(getFileTree(f));	//add all matched files
			}
			if (list.size() > 0) {
				list.add(0, file);				//add this directory to front of list if any files were matched
			}
		} 
		else {
			Matcher m = PATTERN.matcher(file.getName());
			if(m.find()) {		
				list.add(file);					//add file only if its name matches pattern
			}
		}	
		return list;	
	}
}
