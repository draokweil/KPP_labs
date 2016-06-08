package com.draokweil.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Bot extends GameObject {
	
	private final Integer MAX_HEALTH = 3;
	private final Integer DEFAULT_XPOS = 290;
	private final Integer DEFAULT_YPOS = 390;
	public final Integer IMG_SIZE = 30;
	private final Double RANGE = 30d;
	private Random randomGen = new Random();
	private static Double checkpoint;
	private static Double cooldown = 1d;
	private Timeline timeline;
		
	/** Бот для игры SpaceInvaders
	 * @author draokweil
	 * @version 1.0
	*/
	Bot(Boolean s) {
		maxHealth = MAX_HEALTH;
		currentHealth = MAX_HEALTH;
		mode = s;
		status = true;	
        try(InputStream is = Files.newInputStream(Paths.get("res/Ship.png"))){
			img = new ImageView(new Image(is,IMG_SIZE,IMG_SIZE,false,false));
			img.setTranslateX(DEFAULT_XPOS);
			img.setTranslateY(DEFAULT_YPOS);
			img.setEffect(new GaussianBlur(2));
			GameProcess.gamePane.getChildren().add(img);
		}
		catch(IOException e) {
			System.out.println("Error. Couldn't load image");
		}
        if(!mode) getNewPath();
        play();
	}
	
	private void getNewPath() {
		checkpoint = 40 + (double)randomGen.nextInt(GameMenu.W - IMG_SIZE - 40);
		if(!mode){
			GameProcess.saveMaker.addCommand("m");
			GameProcess.saveMaker.addLine(checkpoint.toString());
		}
	}
	
	public void setPath(Double p) {
		Integer missplay = (Integer)randomGen.nextInt(10);
		if (missplay <=2) return;
		checkpoint = p;
		if(checkpoint < 0) {
			checkpoint = 0 + RANGE;
		}
		if(checkpoint > GameMenu.W ) {
			checkpoint = GameMenu.W - 2*RANGE;
		}
		if(!mode){
			GameProcess.saveMaker.addCommand("m");
			GameProcess.saveMaker.addLine(checkpoint.toString());
		}
	}

	
	public void move(){
        if(img.getTranslateX() < checkpoint){
            if(img.getTranslateX()<=GameMenu.W-25) 
            	img.setTranslateX(img.getTranslateX()+2);
        }
        else if(img.getTranslateX() >= checkpoint){
            if(img.getTranslateX()>=5)
            	img.setTranslateX(img.getTranslateX()-2);
        }
        if((Math.abs(img.getTranslateX() - checkpoint) <= RANGE) && !mode) {
        	getNewPath();
        }
	}
	
	private void collisionCheck() {
		for(int i = 0; i < Shot.shotList.size(); i++) {
			double distY = img.getTranslateY() - Shot.shotList.get(i).getYPos();
			double distX = img.getTranslateX() - Shot.shotList.get(i).getXPos();
			if ((Math.abs(distY) < RANGE) && (distX < 0)) {
				setPath(getXPos() - 2*RANGE);
			}
			else if (((Math.abs(distY) < RANGE) && (distX > 0)) && !mode) {
				setPath(getXPos() + 2*RANGE);
			}
		}
	}
	
	public void play(){
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.010), event -> {
			move();
			if(!mode) {
				fire();
				cooldown += 0.010;
				collisionCheck();
			}
			if(status == false) {
				if(!mode) GameProcess.saveMaker.addCommand("gg");
				timeline.stop();
				GameProcess.status = false;
				GameProcess.gameOver();
			}
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
	public void fire(){
		if(status == false ||  cooldown < 1.3) return;
		GameProcess.saveMaker.addCommand("pShot");
		GameProcess.saveMaker.addLine(((Integer)(this.getXPos()+IMG_SIZE/2)).toString());
    	Shot shot = new Shot((int)img.getTranslateX()+IMG_SIZE/2,(int)img.getTranslateY(),true);
    	cooldown = 0d;
	}
	
}