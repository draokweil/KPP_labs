package com.draokweil.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Player extends GameObject {
		
	private final Integer MAX_HEALTH = 3;
	private final Integer DEFAULT_XPOS = 290;
	private final Integer DEFAULT_YPOS = 390;
	public final Integer IMG_SIZE = 30;
	protected boolean moveRight = false;
    protected boolean moveLeft = false;
	private Double cooldown = 1d;
	private Timeline timeline;
	private Timeline timeline2;
	private boolean replayMode;
	
	/** Класс игрока, реализует управление
	 * @author draokweil
	 * @version 1.0
	*/
	Player(boolean m){
		maxHealth = MAX_HEALTH;
		currentHealth = MAX_HEALTH;
		mode = m;
		status = true;	
        try(InputStream is = Files.newInputStream(Paths.get("res/Ship.png"))) {
			img = new ImageView(new Image(is,IMG_SIZE,IMG_SIZE,false,false));
			img.setTranslateX(DEFAULT_XPOS);
			img.setTranslateY(DEFAULT_YPOS);
			img.setEffect(new GaussianBlur(2));
			GameProcess.gamePane.getChildren().add(img);
		}
		catch(IOException e) {
			System.out.println("Error. Couldn't load image");
		}
        if(replayMode == false) {
        	setKeyEvent();
        	Thread thread = new Thread(new Runnable(){
    			public void run() {
    		      play();
    			}
    		});		
           thread.run();
        }
        else gameOverCheck();
	}
	
	private void setKeyEvent()
	{
		GameProcess.gameScene.setOnKeyPressed(event -> {        		
			switch(event.getCode()){
	        case UP:
	        	fire();
	        	break;
	    	case RIGHT:
	    		if(moveRight == false) moveRight();
	    		break;
	        case LEFT:
	        	if(moveLeft == false) moveLeft();
	        	break;
	       	default:
	       		break;
	    	}
	  	});
		GameProcess.gameScene.setOnKeyReleased(event -> {  
			switch(event.getCode()){
	        case UP:
	          	break;
	        case RIGHT: 
	        	if(moveRight == true) stopRight();
	      	    break;
	        case LEFT:
	        	if(moveLeft == true) stopLeft();
	          	break;
	    	default:
	    		break;
	      	}
	  	});
	}
	
	public void gameOverCheck(){
		timeline2 = new Timeline(new KeyFrame(Duration.seconds(0.015), event -> {
			if(status == false) {
				GameProcess.status = false;
				GameProcess.gameOver();
				timeline2.stop();
			}
	    }));
		timeline2.setCycleCount(Animation.INDEFINITE);
		timeline2.play();
	}
	
	public void stopRight(){
		GameProcess.saveMaker.addLine(this.getXPos().toString());
		moveRight=false;
	}
    public void stopLeft(){
		GameProcess.saveMaker.addLine(this.getXPos().toString());
    	moveLeft=false;
    }
	public void moveRight(){
		GameProcess.saveMaker.addCommand("m");
		moveRight=true;
	}
	public void moveLeft(){
		GameProcess.saveMaker.addCommand("m");
		moveLeft=true;
	}
	    
	public void fire(){
		if(status == false || cooldown < 1d) return;
		GameProcess.saveMaker.addCommand("pShot");
		GameProcess.saveMaker.addLine(((Integer)(this.getXPos()+IMG_SIZE/2)).toString());
    	Shot shot = new Shot((int)img.getTranslateX()+IMG_SIZE/2,(int)img.getTranslateY(),true);
    	cooldown = 0d;
	}
	
	public void move(){
        if(moveRight){
            if(img.getTranslateX()<=GameMenu.W-25) 
            	img.setTranslateX(img.getTranslateX()+3);
        }
        if(moveLeft){
            if(img.getTranslateX()>=5)
            	img.setTranslateX(img.getTranslateX()-3);
        }
	}
	
	public void moveTo(Integer xPos){
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.015), event -> {
	        if(img.getTranslateX() < xPos){
	        	img.setTranslateX(img.getTranslateX()+3);
	        }
	        else if(img.getTranslateX() >= xPos){
	        	img.setTranslateX(img.getTranslateX()-3);
	        }
			if(Math.abs(img.getTranslateX() - xPos) < 3) timeline.stop();
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
	public void play(){
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.015), event -> {
			cooldown += 0.015;
			move();  
			if(status == false) {
				GameProcess.saveMaker.addCommand("gg");
				timeline.stop();
				GameProcess.status = false;
				GameProcess.gameOver();
			}
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
}
