package com.draokweil.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FileManager {

	private File file;
	private BufferedReader inp;
	private FileReader reader;
	private FileWriter out;
	
	FileManager(){
		
	}
	
	public void addLine(String filePath, String line) {
		String name = "save\\" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())
		        + ".txt";
		file = new File(name);
		try {
		    out = new FileWriter(file, true);
		    out.write(line);
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}	
	}
	
	public String readLine(String filePath) {
		file = new File(filePath);
		String line = new String();
		try {
		    reader = new FileReader(file);
		    inp = new BufferedReader(reader);
		    int i = 0;
		    while(i !=2){
		    line = new String(inp.readLine());
		    System.out.println(line);
		    i++;
		    }
		    inp.close();
		    reader.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}	
		return line;
	}
	
	public void getStats(ArrayList<String> list){
		Integer counter = 0;
		Boolean inFile = true;
		for( int i = 0; i < list.size(); i++){
			file = new File("save\\" +list.get(i));
			try {
			    reader = new FileReader(file);
			    inp = new BufferedReader(reader);
			    inFile = true;
			    while(inFile){
			    	switch(inp.readLine()){
			    	case "iShot":
			    		counter++;
			    		break;
			    	case "pShot":
			    		counter++;
			    		break;
			    	case "gg":
			    		inFile = false;
			    		break;
			        }
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File sfile = new File("statistics\\stats");
		try {
		    reader = new FileReader(sfile);
		    out = new FileWriter(sfile, false);
		    out.write((Integer.toString(list.size()) + '\n'));  
		    out.write(counter.toString()+ '\n');  
		    out.close();
		    reader.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}	
		System.out.println(counter.toString());
	}
}
