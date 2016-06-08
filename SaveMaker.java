package com.draokweil.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SaveMaker {
	private File file;
	private String filename;
	private FileWriter out;
	private Timeline timeline;
	private Double currentTime = 0d;
	
	SaveMaker(String path){
		
		filename = new String(path);
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.015), event -> {
		currentTime += 0.015;
		if(GameProcess.status == false) {
			timeline.stop();
		}
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	    file = new File(filename);
	    try {
			out = new FileWriter(file, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addLine(String line) {
		file = new File(filename);
		try {
		    out = new FileWriter(file, true);
		    out.write(line + '\n');
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}	
	}
	
	public void addCommand(String line) {
		file = new File(filename);
		try {
		    out = new FileWriter(file, true);
		    out.write(line + '\n' + currentTime.toString() + '\n');
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}	
	}
}
