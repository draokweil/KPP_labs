package com.draokweil.game;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/** Игровой процесс, организует взаимодействие игровых объектов
 * @author draokweil
 * @version 1.0
*/
public class GameProcess {
	
	private InvadersMatrix si;
	public static Pane gamePane;
	public static Scene gameScene;
	private static Stage gameStage;
	
	private File file;
	private BufferedReader inp;
	private FileReader reader;
	public static SaveMaker saveMaker;
	
	public static Integer gameMode;
    public static boolean status = false;
	private static List<GameObject> list = new ArrayList<GameObject>();
	
	private Timeline timeline;
	private Double currentTime = 0d;
	private Double rTime = 0d;
	
	
	public void startGame(Stage primaryStage, Integer mode, String filepath) {
		status = true;
		gameMode = mode;
		gameStage = primaryStage;
		gameScene = new Scene(createGameContent());
    	primaryStage.setScene(gameScene);
    	if (gameMode == 3) replayMode(filepath);
    	else if(gameMode == 1) {
    		saveMaker = new SaveMaker("save\\" 
    		+ new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()));
    		saveMaker.addLine("Player");
    		loadGameObjects();
    	}
    	else if(gameMode == 2) {
    		saveMaker = new SaveMaker("save\\" 
    		+ new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()));
    		saveMaker.addLine("Bot");
    		loadGameObjects();
    	}
	}
	
	private static Parent createGameContent(){
    	gamePane = new Pane();
    	gamePane.setPrefSize(GameMenu.W,GameMenu.H);
    	Rectangle bg = new Rectangle(GameMenu.W+20,GameMenu.H+20);
    	gamePane.getChildren().addAll(bg);
    	return gamePane;
	}
	
	private void loadGameObjects(){
		si = new InvadersMatrix(false);
		list.add(new Shield(gamePane,gameScene,90,310));
		list.add(new Shield(gamePane,gameScene,290,310));
		list.add(new Shield(gamePane,gameScene,490,310));
		if (gameMode == 1) list.add(new Player(false));
		else if (gameMode == 2) list.add(new Bot(false));
	}
	
	public static List<GameObject> getObjectList(){
		return list;
	}
	
	public static void setObjectList(List<GameObject> objList){
		list = objList;
	}
	
	public static void gameOver(){
		gamePane.getChildren().remove(0,gamePane.getChildren().size());
		for(int i = 0; i < list.size(); i++){
			list.get(i).setStatus(false);
			list.get(i).img.setVisible(false);
			list.remove(i);
		}
		for(int i = 0; i < Shot.shotList.size(); i++){
			Shot.shotList.get(i).setStatus(false);
			Shot.shotList.get(i).img.setVisible(false);
			Shot.shotList.remove(i);
		}
		//Main.gameProcess.startGame(gameStage, 2, " ");
		gameOverContent();
	}

    public static void gameOverContent(){
		Text text = new Text(250,200,"GAME OVER");
		text.setFill(Color.WHITESMOKE);
		text.setFont(GameMenu.FONT);
        text.setEffect(new GaussianBlur(2));
        gamePane.getChildren().add(new Rectangle(GameMenu.W+20,GameMenu.H+20));
        gamePane.getChildren().add(text);
        
        gameScene.setOnKeyPressed(event -> {        		
    		switch(event.getCode()){
    		case ENTER:
    			gameScene = new Scene(createGameContent());
    			status = true;
    	        Main.gameMenu.reload();
    		}
        });
        
        return;
    }
        
    private void actionM(Player player, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(readCommand(player,timer));	
		Integer xPos = Integer.valueOf(readCommand(player,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.MILLISECONDS.sleep(2);
		}
		player.moveTo(xPos);
    }
    
    private void actionP(Player player, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(readCommand(player,timer));
		Integer xPos = Integer.valueOf(readCommand(player,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.MILLISECONDS.sleep(2);
		}
		Platform.runLater(new Runnable(){
			public void run(){
				Shot shot = new Shot(player.getXPos()+player.IMG_SIZE/2,
					(int)player.getYPos(),true);
			}
		});
    }
    
    private void actionI(Player player, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(inp.readLine());
		final Integer xP = Integer.valueOf(readCommand(player,timer));
		final Integer yP = Integer.valueOf(readCommand(player,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.NANOSECONDS.sleep(10);
		}
		Platform.runLater(new Runnable(){
			public void run(){
				Shot shot = new Shot(xP,yP,false);
			}
		});
    }
        
    private String readCommand(Player player,GameTimer timer) throws Exception{
    	String buf = inp.readLine();
    	switch(buf){
    	case "m": {
    		actionM(player,timer);
    		buf = readCommand(player,timer);
    		break;
    	}
    	case "pShot": {
    		actionP(player,timer);
    		buf = readCommand(player,timer);
    		break;
    	}
    	case "iShot": {
    		actionI(player,timer);
    		buf = readCommand(player,timer);
    		break;
    	}
    	case "gg": 
    		rTime = Double.valueOf(inp.readLine());
    		while(timer.getTime() < rTime) {
    			TimeUnit.NANOSECONDS.sleep(10);
    		}
    		player.status = false;
    		inp.close();
    	}
    	return buf;
    }
    
    private void actionBM(Bot bot, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(readBCommand(bot,timer));	
		Double xPos = Double.valueOf(readBCommand(bot,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.MILLISECONDS.sleep(2);
		}
		bot.setPath(xPos);
    }
    
    private void actionBP(Bot bot, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(readBCommand(bot,timer));
		Integer xPos = Integer.valueOf(readBCommand(bot,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.MILLISECONDS.sleep(2);
		}
		Platform.runLater(new Runnable(){
			public void run(){
				Shot shot = new Shot(bot.getXPos()+bot.IMG_SIZE/2,
					(int)bot.getYPos(),true);
			}
		});
    }
    
    private void actionBI(Bot bot, GameTimer timer) throws Exception {
    	rTime = Double.valueOf(inp.readLine());
		final Integer xP = Integer.valueOf(readBCommand(bot,timer));
		final Integer yP = Integer.valueOf(readBCommand(bot,timer));
		while(timer.getTime() < rTime) {
			TimeUnit.NANOSECONDS.sleep(10);
		}
		Platform.runLater(new Runnable(){
			public void run(){
				Shot shot = new Shot(xP,yP,false);
			}
		});
    }
    
    private String readBCommand(Bot bot,GameTimer timer) throws Exception{
    	String buf = inp.readLine();
    	switch(buf){
    	case "m": {
    		actionBM(bot,timer);
    		buf = readBCommand(bot,timer);
    		break;
    	}
    	case "pShot": {
    		actionBP(bot,timer);
    		buf = readBCommand(bot,timer);
    		break;
    	}
    	case "iShot": {
    		actionBI(bot,timer);
    		buf = readBCommand(bot,timer);
    		break;
    	}
    	case "gg": 
    		rTime = Double.valueOf(inp.readLine());
    		while(timer.getTime() < rTime) {
    			TimeUnit.NANOSECONDS.sleep(10);
    		}
    		bot.status = false;
    		inp.close();
    	}
    	return buf;
    }
    
    private void replayMode(String filepath){	
		file = new File("save\\" + filepath);
		try {
		    reader = new FileReader(file);
		    inp = new BufferedReader(reader);
		    String buf = inp.readLine();
		    switch(buf){
		    case "Player":
		    	PlayerReplay();//тут чтение и реплей в режиме игрока
		    	break;
		    case "Bot":
		    	BotReplay();//тут чтение и реплей в режиме бота
		    }
		} catch (Exception e) {
		}	
    }
    
    public void BotReplay(){
    	GameTimer timer = new GameTimer();
    	Bot bot = new Bot(true);
    	si = new InvadersMatrix(true);
		list.add(new Shield(gamePane,gameScene,90,310));
		list.add(new Shield(gamePane,gameScene,290,310));
		list.add(new Shield(gamePane,gameScene,490,310));
		list.add(bot);
		Thread thread1 = new Thread(new Runnable(){
			public void run()	
			{
				Integer xPos;
				String line = new String();	
				try {
				    gameMode = 4;	
				    while(bot.status == true) {
				    	readBCommand(bot,timer); 
				    }
				    System.out.println("Vyshel");
				} catch (Exception e) {
				}	
			}
		});		
		thread1.start();
		
    }
    
    public void PlayerReplay(){
    	GameTimer timer = new GameTimer();
    	Player player = new Player(true);
    	si = new InvadersMatrix(true);
		list.add(new Shield(gamePane,gameScene,90,310));
		list.add(new Shield(gamePane,gameScene,290,310));
		list.add(new Shield(gamePane,gameScene,490,310));
		list.add(player);
		Thread thread1 = new Thread(new Runnable(){
			public void run()	
			{
				Integer xPos;
				String line = new String();	
				try {
					while(player.status == true) {
						readCommand(player,timer);
					}
				} catch (Exception e) {
				}	
			}
		});		
		thread1.start();	
    }
}
















