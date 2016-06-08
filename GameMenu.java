package com.draokweil.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/** Игровое меню
 * @author draokweil
 * @version 1.0
*/
public class GameMenu {

    public static Font FONT = Font.font("", FontWeight.BOLD, 24);
	private Media theme;
	private Media ef1;
	private MediaPlayer efPlayer;
	private MediaPlayer mp;
    
    public static final int W = 640;
    public static final int H = 420;
    
    private VBox menuBox,optionsBox,replayBox,statBox,activeBox;
    private static int currentItem = 0;
	private static int currentBox = 0;
    
    private static Stage stage;
    private Scene scene;
    private  Pane root;
    
    
    FileWriter writer;
    ListView list;
     
    public void start(Stage primaryStage){
    	loadMusic();
       	stage = primaryStage;
    	scene = new Scene(createMenuContent());
    	keyController();
    	primaryStage.setTitle("Space Invaders");
    	primaryStage.setScene(scene);
    	primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void reload(){
    	currentItem = 0;
    	currentBox = 0;
    	scene = new Scene(createMenuContent());
    	stage.setScene(scene);
    	keyController();
    }
    
    private void loadMusic() {
    	theme = new Media(new File("res\\theme.mp3").toURI().toString());
    	ef1 = new Media(new File("res\\ef1.mp3").toURI().toString());
    	efPlayer = new MediaPlayer(ef1);
    	mp = new MediaPlayer(theme);
		efPlayer.setVolume(0.7);
		mp.setVolume(1.0);
		mp.setAutoPlay(true);
		mp.setCycleCount(MediaPlayer.INDEFINITE);
    }

    private Parent createMenuContent() {	
    	root = new Pane();
        root.setPrefSize(W,H);
        Rectangle bg = new Rectangle(W+20,H+20);
        root.getChildren().addAll(bg);
    	menuBox = new VBox(10,
                new MenuItem("PLAY"),
                new MenuItem("SAVES"),
                new MenuItem("BOT MODE"),
                new MenuItem("STATS"),
                new MenuItem("OPTIONS"),
                new MenuItem("EXIT"));
    	menuBox.setAlignment(Pos.TOP_CENTER);
        menuBox.setTranslateX(250);
        menuBox.setTranslateY(180);
        
        optionsBox = new VBox(10,
                new MenuItem("MUSIC&SOUND"),
                new MenuItem("GAME MODE"),
                new MenuItem("BACK"));
        optionsBox.setAlignment(Pos.TOP_CENTER);
        optionsBox.setTranslateX(230);
        optionsBox.setTranslateY(210);
        
        replayBox = new VBox(10,
        		new MenuItem("LOAD"),
        		new MenuItem("GET NOTATION"),
        		new MenuItem("JAVA SORT"),
        		new MenuItem("SCALA SORT"),
        		new MenuItem("BACK"));
        replayBox.setTranslateX(50);
        replayBox.setTranslateY(210);     
               
        activeBox = menuBox;
        getMenuItem(0).setActive(true);
            
        Rectangle frame = new Rectangle(205,80,200,90);
        frame.setArcWidth(35);
        frame.setArcHeight(35);
        frame.setStroke(Color.WHITESMOKE);
        frame.setFill(null);
        frame.setEffect(new GaussianBlur(2));
        
        createMenuEf();
    	root.getChildren().addAll(frame,
    			activeBox,
    			LContent("SPACE",230,90,32),
    			LContent("INVADERS",220,115,32));	

        try(InputStream is = Files.newInputStream(Paths.get("res/LOGO.png"))){
			ImageView img = new ImageView(new Image(is,60,60,false,false));
			img.setTranslateX(325);
			img.setTranslateY(75);
			img.setEffect(new GaussianBlur(2));
			root.getChildren().add(img);
		}
		catch(IOException e) {
			System.out.println("Couldn't load image");
		}       
    	return root;
    }
    
    private Node LContent(String name, Integer X, Integer Y, Integer F){   	
    	 HBox logo = new HBox(0);
    	 logo.setAlignment(Pos.CENTER);
    	 logo.setTranslateX(X);
    	 logo.setTranslateY(Y);
         Text text = new Text(name);
         text.setFont(Font.font("", FontWeight.BOLD, F));
         text.setFill(Color.WHITESMOKE);
         text.setEffect(new GaussianBlur(2)); 
         logo.getChildren().add(text);
         return logo;  
    }   
    
    private void createMenuEf(){ 
    	for(int i = 0; i < 75; i++){
    		Circle circle = new Circle(Math.random()*640,Math.random()*420,1+Math.random()*3,Color.GREY);
	    	circle.setEffect(new GaussianBlur(5+Math.random()*5));	
	    	root.getChildren().add(circle);	
    	}
    }
       	       
    private static class MenuItem extends StackPane {  
    	
        private Text text;       
        public MenuItem(String name) {
            setAlignment(Pos.CENTER);
            text = new Text(name);
            text.setFont(FONT);
            text.setEffect(new GaussianBlur(2));
            setActive(false);
            getChildren().addAll(text);
        }
     
        public void setActive(boolean b) {
            text.setFill(b ? Color.LIGHTGREEN : Color.GREY);
        }     
    } 
    
    private static class DirViewer {
    	private static ArrayList<String> list = new ArrayList<String>();
    	private static ObservableList<String> obs;
    	public static ListView listView;
    	public static ListView getDir(String path){
    		list.clear();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
                for (Path file: stream) {
                    if(!file.toFile().isDirectory() ) {
                        list.add(file.getFileName().toString());       
                    }
                }
            } catch (IOException | DirectoryIteratorException x) {
                    System.err.println(x);
            } 
            obs = FXCollections.observableArrayList(list);
            listView = new ListView(obs);
            listView.setOnMouseClicked(event -> {   
            	switch(event.getButton()){
            	case MIDDLE:
            		listView.setDisable(true);
            	}
 	        });
            listView.setOnKeyPressed(event -> {   
            	switch(event.getCode()){
            	case ENTER:
					Main.gameProcess.startGame(stage,3,
						list.get(listView.getSelectionModel().getSelectedIndex()));
	    			break;
            	}
 	        });
            setDefault(listView);
            return listView;
    	}
    	
    	public static ArrayList<String> getSaveList(String path){
    		list.clear();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path))) {
                for (Path file: stream) {
                    if(!file.toFile().isDirectory() ) {
                        list.add(file.getFileName().toString());       
                    }
                }
            } catch (IOException | DirectoryIteratorException x) {
                    System.err.println(x);
            } 
    		return list;
    	}
    	
    	private static void setDefault(ListView list){
    		list.setTranslateX(260);
			list.setTranslateY(200);
			list.setMaxHeight(220);
			list.setVisible(true);
    	}
    	
    	private static ListView javaSort(){
    		if (list.size() <=1) return listView;
    		long beg = System.currentTimeMillis();
    		insertionSort(list);
    		System.out.println(System.currentTimeMillis() - beg);
    		obs.clear();
    		obs.addAll(list);
    		return listView;
    	}
    	
    	private static ListView scalaSort(){
    		if (list.size() <=1) return listView;
    		int[] arr = list.stream().mapToInt(i -> Integer.parseInt(i)).toArray();
    		long beg = System.currentTimeMillis();
    		ScalaSort.qsort(arr, 0, list.size()-1);
    		System.out.println(System.currentTimeMillis() - beg);
    		
    		obs.clear();
    		for(int i = 0; i < list.size(); i++){
    			obs.add(Integer.toString(arr[i]));
    		}
    		return listView;
    	}
    	
        public static void insertionSort(ArrayList<String> list) {
            for(int i = 1; i < list.size(); i++){
            	Integer currElem = Integer.parseInt(list.get(i));
                Integer prevKey = i - 1;
                while(prevKey >= 0 && Integer.parseInt(list.get(prevKey)) < currElem){
                	list.set(prevKey + 1,list.get(prevKey));
                    prevKey--;
                }
                list.set(prevKey + 1, currElem.toString());
            }
        }
    }
               
    private MenuItem getMenuItem(int index) {
        return (MenuItem)activeBox.getChildren().get(index);
    }
    
    private void setActiveBox(VBox tmp) {
    	root.getChildren().remove(activeBox);
    	activeBox = tmp;
    	root.getChildren().add(activeBox);
    	}
    
    private void keyController() {
    	scene.setOnKeyPressed(event -> {        		
    		switch(event.getCode()){
    		case ENTER:
    			efPlayer.seek(efPlayer.getStartTime());
    			efPlayer.play();
    			switch(currentBox){
    	    	case 0:	
    	    		switch(currentItem){
    	    		case 0:
    	    			currentBox = 2;
    	    			Main.gameProcess = new GameProcess();
						Main.gameProcess.startGame(stage,1,"");
    	    			break;
    	    		case 1:
    	    			getMenuItem(currentItem).setActive(false);
    	    			setActiveBox(replayBox);
    	    			currentItem = 0;
    	    			getMenuItem(currentItem).setActive(true);
    	    			currentBox = 3;
    	    			root.getChildren().add(DirViewer.getDir("save\\"));
    	    			break;
    	    		case 2:
    	    			currentBox = 2;
    	    			Main.gameProcess = new GameProcess();
						Main.gameProcess.startGame(stage,2,"");
    	    			break;
    	    		case 3:
    	    			FileManager fm = new FileManager();
    	    			fm.getStats(DirViewer.getSaveList("save\\"));
    	    			getMenuItem(currentItem).setActive(false); 
    	    			statBox = new VBox(10,
    	    	        		new MenuItem("GAMES PLAYED: " + DirViewer.list.size()),
    	    	        		new MenuItem("BULLETS SPENT: " + ScalaStats.getStats()),
    	    	        		new MenuItem("GAME MODE: " + "EASY"));
    	    	        statBox.setTranslateX(100);
    	    	        statBox.setTranslateY(210);  
   	    		        setActiveBox(statBox);
   	    		        currentItem = 0;
    	    		    currentBox = 5;      
    	    			break;
    	    		case 4:
    	    			getMenuItem(currentItem).setActive(false); 
   	    		        setActiveBox(optionsBox);
   	    		        getMenuItem(0).setActive(true); 
   	    		        currentItem = 0;
    	    		    currentBox = 1;      
    	    		    break;
    	    		case 5:
    	    			stage.close();
    	    			break;	
    	    		
    	    		}  
    	    	case 1:
    	    		switch(currentItem){
	    			case 0:
	    			    break;
	    			case 1:
	    			    break;
	    			case 2:
	    			    getMenuItem(currentItem).setActive(false); 
	    			    setActiveBox(menuBox);       
	    			    getMenuItem(0).setActive(true); 
	    			    currentItem = 0;
	    			    currentBox = 0;
	    			    break;   
    	    		}
    	    	case 3:
    	    		switch(currentItem){
	    			case 0:	
	    			    break;
	    			case 1:
	    				String str = "save\\" + DirViewer.listView.getSelectionModel().getSelectedItem();
	    				ScalaStats.getNotation(str);
	    				break;
	    			case 2:
	    				DirViewer.javaSort();
	    				DirViewer.listView.refresh();
	    			    break;
	    			case 3:
	    				DirViewer.scalaSort();
	    				DirViewer.listView.refresh();
	    			    break;  
	    			case 4:
	    				getMenuItem(currentItem).setActive(false); 
	    			    setActiveBox(menuBox);       
	    			    getMenuItem(0).setActive(true); 
	    			    root.getChildren().remove(DirViewer.listView);
	    			    currentItem = 0;
	    			    currentBox = 0;
	    			    break;   
    	    		}
    			}
    			break;
    		case UP:
    			if(currentBox == 5) break;
    			if(currentItem > 0){
    				getMenuItem(currentItem).setActive(false);
    				getMenuItem(--currentItem).setActive(true);
    	    		efPlayer.seek(efPlayer.getStartTime());
    	    		efPlayer.play();
                }
                if(currentItem == 0 && currentBox == 3){
                	DirViewer.listView.setDisable(false);
                };
    			break;
    		case DOWN:
    			if(currentBox == 5) break;
    			if (currentItem < activeBox.getChildren().size()-1){
    				getMenuItem(currentItem).setActive(false);
                    getMenuItem(++currentItem).setActive(true);
    	    		efPlayer.seek(efPlayer.getStartTime());
    	    		efPlayer.play();
                }
    			break;
    		case ESCAPE:
    			getMenuItem(currentItem).setActive(false); 
    			setActiveBox(menuBox);       
    			getMenuItem(0).setActive(true); 
    			root.getChildren().remove(DirViewer.listView);
    			currentItem = 0;
    			currentBox = 0;	
    			break;
			default:
				break;		
    		}
    				
        });
    }


}
