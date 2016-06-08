package com.draokweil.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/** Пуля
 * @author draokweil
 * @version 1.0
*/
public class Shot extends GameObject{
	
	public static List<Shot> shotList = new ArrayList<Shot>();
	private final Double IMG_SIZE = 3.0;
	private boolean fraction;	
	private Timeline timeline;
	/** 
	 * @param xP - координата X
	 * @param yP - координата Y
	 * @param f - фракция
	*/
	Shot(Integer xP,Integer yP,boolean f){	
		fraction = f;	
        try(InputStream is = Files.newInputStream(Paths.get("res/Shot.png"))){
			img = new ImageView(new Image(is,IMG_SIZE,IMG_SIZE,false,false));
			img.setTranslateX(xP);
			img.setTranslateY(yP);
			img.setEffect(new GaussianBlur(2));
		}
		catch(IOException e) {
			System.out.println("Error.");
		}
		GameProcess.gamePane.getChildren().add(img);
		shotList.add(this);
		play();
	}
	
	public void play(){
		timeline = new Timeline(new KeyFrame(Duration.seconds(0.0075), event -> {
		if(GameProcess.status == false){
			timeline.stop();
		}
		if(status != false){ 
			move();
			collisionTest();
			InvadersMatrix.collisionControl(this);
		}
		
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
	public boolean getFraction(){
		return fraction;
	}
	
	public void move(){
        if(fraction == true){
            if(img.getTranslateY()>=0) 
            	img.setTranslateY(img.getTranslateY()-4);
        }
        if(fraction == false){
            if(img.getTranslateY()<=440) 
            	img.setTranslateY(img.getTranslateY()+4);
        }
	}    

	public void collisionTest(){
		List<GameObject> list = GameProcess.getObjectList();
		if(!(img.getBoundsInParent().intersects(0,0,640,430))){
			GameProcess.gamePane.getChildren().remove(img);
			shotList.remove(this);
			status = false;
		}
		for(int i = 0; i < GameProcess.getObjectList().size(); i++){
			if(!(img.getBoundsInParent().intersects(list.get(i).img.getBoundsInParent()))){
				continue;
			}
			else if (list.get(i) instanceof Shot){
				continue;
			}
			else if ((list.get(i) instanceof Player || list.get(i) instanceof Bot) && fraction == true){
				continue;
			}
			else if (list.get(i) instanceof Shield){
				GameProcess.gamePane.getChildren().remove(img);
				shotList.remove(this);
				status = false;
			}
			else {
				if(list.get(i).mode == true) continue;
				GameProcess.gamePane.getChildren().remove(img);
				GameProcess.gamePane.getChildren().remove(list.get(i).img);
				list.get(i).status = false;
				shotList.remove(this);
				status = false;
			}
		 
		}
		GameProcess.setObjectList(list);
	}
	

}