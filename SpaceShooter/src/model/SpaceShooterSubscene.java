package model;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class SpaceShooterSubscene extends SubScene{

	//private final static String FONT_PATH = "src/model/resources/kenvector_future.ttf";
	private final static String BACKGROUND_IMAGE = "/model/resources/yellow_panel.png";
	
	private static boolean isHidden = true;
	
	public SpaceShooterSubscene() {
		super(new AnchorPane(), 600, 400);
		prefWidth(600);
		prefHeight(400);
		BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 600, 400, false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
		
		AnchorPane root2 = (AnchorPane)this.getRoot();
		
		root2.setBackground(new Background(image));
		
		setLayoutX(1024);
		setLayoutY(280);

	}

	public void moveSubScene(){
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(0.3));
		transition.setNode(this);
		
		if(isHidden){
			transition.setToX(-676);
			isHidden = false;
		} else {
			transition.setToX(1024);
			isHidden = true;
		}
		
		transition.play();
	}
	
	public AnchorPane getPane(){
		return (AnchorPane)this.getRoot();
	}

}

