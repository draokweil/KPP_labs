package com.draokweil.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/** Класс противника, реализует его поведение
 * @author draokweil
 * @version 1.0
*/
public class Invader extends GameObject{
	public static final Integer IMG_H = 22;
	public static final Integer IMG_W = 30;
	private Integer currentTurn = 1;
	private static Integer massTurn = 1;
	private static Image image1;
	private static Image image2;
	private Integer imageStatus = 1;
	
	
	/** Конструктор
	 * @param xP - координата X
	 * @param yP - координата Y 
	*/
	Invader(Integer xP,Integer yP) {
		 try(InputStream is = Files.newInputStream(Paths.get("res/Invader.png"))){
			 	image1 = new Image(is,IMG_W,IMG_H,false,false);
			}
			catch(IOException e) {
			}
		 try(InputStream is = Files.newInputStream(Paths.get("res/Invader2.png"))){
			 	image2 = new Image(is,IMG_W,IMG_H,false,false);
			 	}
			catch(IOException e) {
			}
		img = new ImageView(image1);
		img.setTranslateX(xP);
		img.setTranslateY(yP);
		img.setEffect(new GaussianBlur(2));
		GameProcess.gamePane.getChildren().add(img);
	}
	
	public static Integer getMT() {
		return massTurn;
	}
	
	public static void setMT(Integer d) {
		massTurn = d;
	}	
	
	public void nextTurn() {
		currentTurn = (currentTurn == 4) ? 1 : ++currentTurn;
	}
	
	public Integer getCT() {
		return currentTurn;
	}
	
	public void setCT(Integer ct){
		currentTurn = ct;
	}
	
	public void nextImg() {
		if(imageStatus == 1){
			imageStatus = 2;
			img.setImage(image2);
	    }
		else if(imageStatus == 2){
			imageStatus = 1;
			img.setImage(image1);
		}
	}
	
	/** Реализует движение */
	public void move(){
		switch(currentTurn){
		case 1:{
          img.setTranslateX(img.getTranslateX()+10);
          if(img.getTranslateX()+IMG_W >= 600) nextTurn();
          break;
		}
		case 2:{
		  img.setTranslateY(img.getTranslateY()+15);
	      nextTurn();
	      break;
		}
		case 3:{
	      img.setTranslateX(img.getTranslateX()-10);
          if(img.getTranslateX()+IMG_W <= 60) nextTurn();
	      break;
		}
		case 4:{
		  img.setTranslateY(img.getTranslateY()+15);
		  nextTurn();
		  break;
		}
		default: break;
		}
	}
		
	public void fire(){
		if(GameProcess.status == false) return;
		Shot shot = new Shot((int)img.getTranslateX()+IMG_W/2,
				(int)img.getTranslateY()+IMG_H/2, false);
		if(GameProcess.gameMode == 1 || GameProcess.gameMode == 2) {
			GameProcess.saveMaker.addCommand("iShot");
			GameProcess.saveMaker.addLine(((Integer)(this.getXPos()+IMG_W/2)).toString());
			GameProcess.saveMaker.addLine(((Integer)(this.getYPos()+IMG_H/2)).toString());
		}
	}
}
