package edu.isu.tools;

import java.io.File;
import java.io.FilenameFilter;

public class JsonFileFilter implements FilenameFilter {
	
	public boolean containUDDI(String file) {
		return file.endsWith(".json");
	}
	
	@Override
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		return containUDDI(name);
	}
	
	
}
