package com.draokweil.game;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static GameMenu gameMenu = new GameMenu();
	public static GameProcess gameProcess;
	
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
    	gameMenu.start(primaryStage);	
    }
          
}
