package view;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.SHIP;
import model.SmallInfoLabel;

public class GameViewManager {
	
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	
	private static final int GAME_WIDTH = 600;
	private static final int GAME_HEIGHT = 800;
	
	private Stage menuStage;
	private ImageView ship;
	
	private boolean isLeftKeyPressed;
	private boolean isRightKeyPressed;
	private int angle;
	private AnimationTimer gameTimer;
	
	private GridPane gridPane1;
	private GridPane gridPane2;
	private final static String BACKGROUND_IMAGE = "view/resources/deep_blue.png";
	private final static String BACKGROUND_IMAGE2 = "view/resources/purple.png";
	
	private final static String METEOR_BROWN_IMAGE = "view/resources/meteorBrown_med3.png";
	private final static String METEOR_GREY_IMAGE = "view/resources/meteorGrey_med2.png";
	
	private ImageView[] brownMeteors;
	private ImageView[] greyMeteors;
	Random randomPositionGenerator;
	
	private ImageView star;
	private SmallInfoLabel pointsLabel;
	private ImageView[] playerLifes;
	private int playerLife;
	private int points = 0;
	private final static String GOLD_STAR_IMAGE = "view/resources/star_gold.png";
	
	private final static int STAR_RADIUS = 12;
	private final static int SHIP_RADIUS = 27;
	private final static int MEDIUM_METEOR_RADIUS = 20;
	
	public GameViewManager(){
		initalizeStage();
		createKeyListeners();
		randomPositionGenerator = new Random();
		
	}

	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT){
					isLeftKeyPressed = true;
				}else if(event.getCode() == KeyCode.RIGHT){
					isRightKeyPressed = true;
				}
			}
		});
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.LEFT){
					isLeftKeyPressed = false;
				}else if(event.getCode() == KeyCode.RIGHT){
					isRightKeyPressed = false;
				}
			}
		});
		
	}

	private void initalizeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		
	}
	
	public void createNewGame(Stage menuStage, SHIP choosenShip){
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBackground();
		createShip(choosenShip);
		createGameElements(choosenShip);
		createGameLoop();
		gameStage.show();
		
	}
	
	private void createGameElements(SHIP choosenShip){
		playerLife = 2;
		star = new ImageView(GOLD_STAR_IMAGE);
		setNewElementsPosition(star);
		gamePane.getChildren().add(star);
		pointsLabel = new SmallInfoLabel("POINTS : 00");
		pointsLabel.setLayoutX(430);
		pointsLabel.setLayoutY(20);
		gamePane.getChildren().add(pointsLabel);
		playerLifes = new ImageView[playerLife + 1];
		
		for(int i = 0; i < playerLifes.length; i++){
			playerLifes[i] = new ImageView(choosenShip.getUrlLife());
			playerLifes[i].setLayoutX(455 + (i * 50));
			playerLifes[i].setLayoutY(80);
			gamePane.getChildren().add(playerLifes[i]);
		}
		
		brownMeteors = new ImageView[3];
		for(int i = 0; i < brownMeteors.length; i++){
			brownMeteors[i] = new ImageView(METEOR_BROWN_IMAGE);
			setNewElementsPosition(brownMeteors[i]);
			gamePane.getChildren().add(brownMeteors[i]);
		}
		greyMeteors = new ImageView[3];
		for(int i = 0; i < greyMeteors.length; i++){
			greyMeteors[i] = new ImageView(METEOR_GREY_IMAGE);
			setNewElementsPosition(greyMeteors[i]);
			gamePane.getChildren().add(greyMeteors[i]);
		}
	}
	
	private void moveGameElements(){
		star.setLayoutY(star.getLayoutY() + 5);
		
		for(int i = 0; i < brownMeteors.length; i++){
			brownMeteors[i].setLayoutY(brownMeteors[i].getLayoutY()+7);
			brownMeteors[i].setRotate(brownMeteors[i].getRotate()+4);		
		}
		
		for(int i = 0; i < greyMeteors.length; i++){
			greyMeteors[i].setLayoutY(greyMeteors[i].getLayoutY()+7);
			greyMeteors[i].setRotate(greyMeteors[i].getRotate()+4);		
		}
	}
	
	private void checkIfElemetsAreBehindTheShipAndRelocate(){
		if(star.getLayoutY() > 1200)
			setNewElementsPosition(star);
		
		for(int i = 0; i < brownMeteors.length; i++){
			if(brownMeteors[i].getLayoutY() > 900){
				setNewElementsPosition(brownMeteors[i]);
			}
		}
		
		for(int i = 0; i < greyMeteors.length; i++){
			if(greyMeteors[i].getLayoutY() > 900){
				setNewElementsPosition(greyMeteors[i]);
			}
		}
	}
	
	private void setNewElementsPosition(ImageView image){
		image.setLayoutX(randomPositionGenerator.nextInt(370));
		image.setLayoutY(-(randomPositionGenerator.nextInt(3200)+600));
	}
	
	private void createShip(SHIP choosenShip){
		ship = new ImageView(choosenShip.getUrl());
		ship.setLayoutX(GAME_WIDTH/2);
		ship.setLayoutY(GAME_HEIGHT - 90);
		gamePane.getChildren().add(ship);
	}
	
	private void createGameLoop(){
		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				moveBackground();
				moveGameElements();
				checkIfElemetsAreBehindTheShipAndRelocate();
				checkIfElementsCollide();
				moveShip();
			}
		};
		gameTimer.start();
	}
	
	private void moveShip(){
		if(isLeftKeyPressed && !isRightKeyPressed){
			if(angle > -30)
				angle -= 10;
			ship.setRotate(angle);
			if(ship.getLayoutX() > -20)
				ship.setLayoutX(ship.getLayoutX() - 4);
		}
		if(isRightKeyPressed && !isLeftKeyPressed){
			if(angle < 30)
				angle += 10;
			ship.setRotate(angle);
			if(ship.getLayoutX() < 522)
				ship.setLayoutX(ship.getLayoutX() + 4);
		}
		if(!isRightKeyPressed && !isLeftKeyPressed){
			if(angle < 0)
				angle += 5;
			else if (angle >0)
				angle -= 5;
			ship.setRotate(angle);
		}
		if(isRightKeyPressed && isLeftKeyPressed){
			if(angle < 0)
				angle += 5;
			else if (angle >0)
				angle -= 5;
			ship.setRotate(angle);
		}
	}
	
	private void createBackground(){
		gridPane1 = new GridPane();
		gridPane2 = new GridPane();
		
		for(int i = 0; i < 12; i++){
			ImageView backgroundImage1 = new ImageView(BACKGROUND_IMAGE);
			ImageView backgroundImage2 = new ImageView(BACKGROUND_IMAGE2);
			GridPane.setConstraints(backgroundImage1, i% 3, i / 3);
			GridPane.setConstraints(backgroundImage2, i% 3, i / 3);
			gridPane1.getChildren().add(backgroundImage1);
			gridPane2.getChildren().add(backgroundImage2);
		}
		gridPane2.setLayoutY(-1024);
		
		gamePane.getChildren().addAll(gridPane1, gridPane2);
		
		
	}
	
	private void moveBackground(){
		gridPane1.setLayoutY(gridPane1.getLayoutY() + 5);
		gridPane2.setLayoutY(gridPane2.getLayoutY() + 5);
		
		if(gridPane1.getLayoutY() >= 1020)
			gridPane1.setLayoutY(-1024);
		
		if(gridPane2.getLayoutY() >= 1020)
			gridPane2.setLayoutY(-1024);
	}
	
	private void checkIfElementsCollide(){
		if(SHIP_RADIUS + STAR_RADIUS > calculateDistance(ship.getLayoutX() + 49, star.getLayoutX() + 15, ship.getLayoutY() + 37,  star.getLayoutY() + 15)){
			setNewElementsPosition(star);
			points += 100;
			String textToSet = "POINTS : ";
			pointsLabel.setText(textToSet + points);
		}
		
		for(int i = 0; i < brownMeteors.length; i++){
			if(MEDIUM_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, brownMeteors[i].getLayoutX() + 20, ship.getLayoutY() + 37, brownMeteors[i].getLayoutY() + 20)){
				setNewElementsPosition(brownMeteors[i]);
				removeLife();
			}		
		}
		
		for(int i = 0; i < greyMeteors.length; i++){
			if(MEDIUM_METEOR_RADIUS + SHIP_RADIUS > calculateDistance(ship.getLayoutX() + 49, greyMeteors[i].getLayoutX() + 20, ship.getLayoutY() + 37, greyMeteors[i].getLayoutY() + 20)){
				setNewElementsPosition(greyMeteors[i]);
				removeLife();
			}		
		}
	}
	
	private void removeLife(){
		gamePane.getChildren().remove(playerLifes[playerLife]);
		playerLife--;
		if(playerLife < 0){
			gameStage.close();
			gameTimer.stop();
			menuStage.show();
		}
	}
	
	private double calculateDistance(double x1, double x2, double y1, double y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

}
