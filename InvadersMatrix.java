package com.draokweil.game;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


/** Совокупность противников. Необходим для синхронизации
 * @author draokweil
 * @version 1.0
*/
public class InvadersMatrix {
	
	private final Integer DEFAULT_XPOS = 60;
	private final Integer DEFAULT_YPOS = 30;
	private final Integer DISTANTION = 10;
	private static final Integer H = 5;
	private static final Integer W = 9;
	private static final Invader [][] iMatrix = new Invader[H][W];
	private Random randomGen = new Random();
	private Timeline timeline;
	private boolean replayMode;
	
	InvadersMatrix(boolean mode){	
		replayMode = mode;
		for(int i = 0; i < H; i++){
		  for(int j = 0; j < W; j++){
			iMatrix[i][j] = new Invader(DEFAULT_XPOS+(DISTANTION+Invader.IMG_W)*j,
						DEFAULT_YPOS+(DISTANTION+Invader.IMG_H)*i);
			}
		}
		play();
	}
			
	public void play(){
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.75), event -> {
			if(GameProcess.status == false){
				timeline.stop();
			}
			move();
			if (replayMode == false) fire();
			turnControl();
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
	public static void collisionControl(Shot shot){
		for(int i = 0; i < H; i++){
		  for(int j = 0; j < W; j++){ 
			  if(iMatrix[i][j].status == false || shot.status == false) continue;
			  else if(iMatrix[i][j].img.getBoundsInParent()
			    .intersects(shot.img.getBoundsInParent()) && shot.getFraction() == true){
					GameProcess.gamePane.getChildren().remove(shot.img);
					GameProcess.gamePane.getChildren().remove(iMatrix[i][j].img);
					iMatrix[i][j].status = false;
					shot.status = false;
					return;
			  }
		  }
		}
	}
	
	public void move(){
		Thread thread = new Thread(new Runnable(){
			public void run() {
				  for(int i = 0; i < H; i++){
					  for(int j = 0; j < W; j++){ 
						  iMatrix[i][j].move();
						  iMatrix[i][j].nextImg();
					  }
				  }
			}
		});		
       thread.run();
	}
	
	public void turnControl(){
		for(int i = 0; i < H; i++){
			  for(int j = 0; j < W; j++){ 
				  if(iMatrix[i][j].getCT() != Invader.getMT()){
					  Invader.setMT(iMatrix[i][j].getCT());
					  setCT();
				  }
			  }
			}
	}
	
	public void setCT(){
		for(int i = 0; i < H; i++){
			  for(int j = 0; j < W; j++){ 
				  iMatrix[i][j].setCT(Invader.getMT());
			  }
			}
	}
	
	public void fire(){
		
		for(int i = 0; i < H; i++){
		    for(int j = 0; j < W; j++){
			    if(randomGen.nextInt(150) < 6 && iMatrix[i][j].status == true){
				    iMatrix[i][j].fire();
			    }
		    }
		}
	}
}
