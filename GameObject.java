package com.draokweil.game;

import javafx.scene.image.ImageView;

public class GameObject {

	protected Integer maxHealth;
	protected Integer currentHealth;	
	protected ImageView img;
	protected boolean status = true;
	protected boolean mode;
	
	/** Игровой объект, служит родителем для Player,Invader
	 * @author draokweil
	 * @version 1.0
	*/
	GameObject(){
		maxHealth = 0;
		currentHealth = 0;	
	}
	
	public void setStatus(boolean s){
		status = s;
	}
	
	public Integer getXPos(){
		return (int)img.getTranslateX();
	}
	
	public Integer getYPos(){
		return (int)img.getTranslateY();
	}
	
	public void setXPos(Integer x){
		img.setTranslateX(x);
	}
	
	public boolean isAlive(){
		return status;
	}
	
}
