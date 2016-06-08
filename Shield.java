package com.draokweil.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/** Защитный блок
 * @author draokweil
 * @version 1.0
*/
public class Shield extends GameObject{

	private static final Integer IMG_H = 25;
	private static final Integer IMG_W = 50;
	
	Shield(Pane pane,Scene scene,Integer xP,Integer yP){
		 try(InputStream is = Files.newInputStream(Paths.get("res/Barrier.png"))){
				img = new ImageView(new Image(is,IMG_W,IMG_H,false,false));
				img.setTranslateX(xP);
				img.setTranslateY(yP);
				img.setEffect(new GaussianBlur(2));
				pane.getChildren().add(img);
			}
			catch(IOException e) {
				System.out.println("Error.");
			}
	}
	
	
}
