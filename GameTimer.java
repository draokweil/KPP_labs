package com.draokweil.game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

	private Double currentTime = 0d;
	private TimerTask counter;
	private Timer timer;
	GameTimer() {
		timer = new Timer();
		counter = new TimerTask() {
			public void run() {
				currentTime+= 0.008;
			}
		};
		timer.scheduleAtFixedRate(counter, 0, 8);
	}
	
	Double getTime() {
		return currentTime;
	}
}
