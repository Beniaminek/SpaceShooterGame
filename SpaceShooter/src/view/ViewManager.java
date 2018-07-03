package view;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.InfoLabel;
import model.SHIP;
import model.ShipPicker;
import model.SpaceShooterButton;
import model.SpaceShooterSubscene;

public class ViewManager {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	private AnchorPane mainPane;
	private Scene mainScene;
	private Stage mainStage;
	
	private final static int MENU_BUTTON_START_X = 100;
	private final static int MENU_BUTTON_START_Y = 150;
	
	private SpaceShooterSubscene credistsSubScene;
	private SpaceShooterSubscene helpSubScene;
	private SpaceShooterSubscene scoreSubScene;
	private SpaceShooterSubscene shipChooserScene;
	
	private SpaceShooterSubscene sceneToHide;
	
	List<SpaceShooterButton> menuButtons;
	
	List<ShipPicker> shipList;
	private SHIP choosenShip;
	
	public ViewManager(){
		menuButtons = new ArrayList<>();
		mainPane = new AnchorPane();
		mainScene = new Scene(mainPane, WIDTH, HEIGHT);
		mainStage = new Stage();
		mainStage.setScene(mainScene);
		createButtons();
		createBackground();
		createLogo();
		createSubScene();
		
		
	}
	
	private void showSubScene(SpaceShooterSubscene subScene){
		if(sceneToHide != null){
			sceneToHide.moveSubScene();
		}
		subScene.moveSubScene();
		sceneToHide = subScene;
	}
	
	private void createSubScene(){
		credistsSubScene = new SpaceShooterSubscene();
		mainPane.getChildren().add(credistsSubScene);
		
		helpSubScene = new SpaceShooterSubscene();
		mainPane.getChildren().add(helpSubScene);
		
		scoreSubScene = new SpaceShooterSubscene();
		mainPane.getChildren().add(scoreSubScene);
		
		createShipChooserSubScene();
	}
	
	private void createShipChooserSubScene() {
		shipChooserScene = new SpaceShooterSubscene();
		mainPane.getChildren().add(shipChooserScene);
		
		InfoLabel choosenShipLabel = new InfoLabel("Choose your SHIP");
		choosenShipLabel.setLayoutX(110);
		choosenShipLabel.setLayoutY(25);
		shipChooserScene.getPane().getChildren().add(choosenShipLabel);
		shipChooserScene.getPane().getChildren().add(createShipToChoose());
		shipChooserScene.getPane().getChildren().add(createButtonToStart());
	}
	
	private HBox createShipToChoose(){
		HBox box = new HBox();
		box.setSpacing(20);
		shipList = new ArrayList<>();
		for(SHIP ship : SHIP.values()){
			ShipPicker shipToPick = new ShipPicker(ship);
			shipList.add(shipToPick);
			box.getChildren().add(shipToPick);
			shipToPick.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					for(ShipPicker ship : shipList){
						ship.setIsCicrcleChoosen(false);
					}
					shipToPick.setIsCicrcleChoosen(true);
					choosenShip = shipToPick.getShip();
				}
			});
		}
		box.setLayoutX(300 - (118*2));
		box.setLayoutY(100);
		return box;
	}
	
	private SpaceShooterButton createButtonToStart(){
		SpaceShooterButton startButton = new SpaceShooterButton("START");
		startButton.setLayoutX(195);
		startButton.setLayoutY(300);
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(choosenShip != null){
					GameViewManager gameManager = new GameViewManager();
					gameManager.createNewGame(mainStage, choosenShip);
				}
			}
		});
		return startButton;
	}

	public Stage getMainStage(){
		return mainStage;
	}
	
	private void addMenuButton(SpaceShooterButton button){
		button.setLayoutX(MENU_BUTTON_START_X);
		button.setLayoutY(MENU_BUTTON_START_Y + menuButtons.size() * 100);
		menuButtons.add(button);
		mainPane.getChildren().add(button);
	}
	
	private void createButtons(){
		createStartButton();
		createScorsButton();
		createHelpButton();
		createCreditsButton();
		createExitButton();
	}
	
	private void createStartButton(){
		SpaceShooterButton startButton = new SpaceShooterButton("PLAY");
		addMenuButton(startButton);
		startButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				showSubScene(shipChooserScene);
			}
		});
	}
	
	private void createScorsButton(){
		SpaceShooterButton scoreButton = new SpaceShooterButton("SCORES");
		addMenuButton(scoreButton);
		scoreButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				showSubScene(scoreSubScene);
			}
		});
	}
	
	private void createHelpButton(){
		SpaceShooterButton helpButton = new SpaceShooterButton("HELP");
		addMenuButton(helpButton);
		helpButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				showSubScene(helpSubScene);
			}
		});
	}
	
	private void createCreditsButton(){
		SpaceShooterButton creditsButton = new SpaceShooterButton("CREDITS");
		addMenuButton(creditsButton);
		creditsButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				showSubScene(credistsSubScene);
			}
		});
	}
	
	private void createExitButton(){
		SpaceShooterButton exitButton = new SpaceShooterButton("EXIT");
		addMenuButton(exitButton);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mainStage.close();
			}
		});
	}
	
	private void createBackground(){
		Image backgroundImage = new Image("/view/resources/deep_blue.png", 256, 256, false, true);
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
		mainPane.setBackground(new Background(background));
	}
	
	private void createLogo(){
		ImageView logo = new ImageView("/view/resources/JOYSTICK_background.png");
		logo.setLayoutX(400);
		logo.setLayoutY(70);
		mainPane.getChildren().add(logo);
	}
}
