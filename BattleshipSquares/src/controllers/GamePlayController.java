package controllers;


import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import application.Board;
import application.ShipPart;
import application.Main;
import application.Ship;



//extends Application

public class GamePlayController  implements Initializable {

	@FXML
    private BorderPane windowRoot;

    @FXML
    private HBox buttonBarHBox;

    @FXML
    private Button homeButton;

    @FXML
    private Button exitButton;

    @FXML
    private ImageView playerImage;

    @FXML
    private ImageView opponentImage;

    @FXML
    private HBox oppcarrier;

    @FXML
    private HBox oppbattleship;

    @FXML
    private HBox oppcruiser;

    @FXML
    private HBox oppsubmarine;

    @FXML
    private HBox oppdestroyer;

    @FXML
    private HBox carrier;

    @FXML
    private HBox battleship;

    @FXML
    private HBox cruiser;

    @FXML
    private HBox submarine;

    @FXML
    private HBox destroyer;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

	//	public BattleshipMain() {
	//		super();
	//	}
	public Button continueButton;
	
	
	
	//public Text text1, text2;

	//private double xOffset = 0;
	//private double yOffset = 0; //staring  pos forn window drags

	private boolean running = false;
	private Board computerBoard, playerBoard;

	private int shipsToPlace = 5;

	private boolean computerTurn = false;
	//MediaPlayer mediaPlayer;

	private Random random = new Random();
	//BorderPane windowRoot = new BorderPane();



	BoxBlur blur = new BoxBlur();
	Glow glow = new Glow();
	Stage gameStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//set up background music
		String battleshipSquaresTheme = "battleshipSquaresTheme.wav";
		Media sound = new Media(Paths.get(battleshipSquaresTheme).toUri().toString());
		Main.mediaPlayer = new MediaPlayer(sound);
		Main.mediaPlayer.play();

		windowRoot.setStyle("-fx-background-color: black;");
		windowRoot.getStylesheets().add("GamePlayScene.css");

		setButtonBar();
		setTopArea();


		//	windowRoot.setPadding(new Insets(20,20,0,20));

		//////need to make changes do it from ships


		playerBoard = new Board(false, event -> {
			if (running)
				return;

			ShipPart shipPart = (ShipPart) event.getSource();
			if (playerBoard.putShipInPlace(new Ship(shipsToPlace, event.getButton()), shipPart.x, shipPart.y)) {
				if (--shipsToPlace == 0) {
					startGame();
				}
			}
		});

		computerBoard = new Board(true, event -> {
			if (!running)
				return;

			ShipPart shipPart = (ShipPart) event.getSource();
			if (shipPart.shipPartWasHit)
				return;

			computerTurn = !shipPart.hitShipPart();

			if (computerBoard.ships == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("YOU WIN");
				alert.showAndWait();

				System.out.println("YOU WIN");
			}

			if (computerTurn)
				computerMove();
		});


		HBox boards = new HBox(40, computerBoard, playerBoard);
		boards.setPadding(new Insets(0,20,0,20));
		windowRoot.setCenter(boards);





	}
	private void setTopArea() {
		opponentImage .setImage(new Image(getClass().getClassLoader().getResource("blueEnemies (1).png").toString()));
		//	opponentImage.setFitHeight(100);
		//	opponentImage.setFitWidth(250);
		//	opponentImage.setOpacity(9.0);
		Image logoBattleship =  new Image(getClass().getClassLoader().getResource("theme1.png").toString());
		playerImage.setImage(new Image(getClass().getClassLoader().getResource(Main.profileImg).toString()));
		int en = random.nextInt(5-1)+1;
		opponentImage.setImage(new Image(getClass().getClassLoader().getResource("blueEnemies ("+en+").png").toString()));

		if (Main.colorTheme != null) {
			switch(Main.colorTheme){
			case RED :
				logoBattleship =  new Image(getClass().getClassLoader().getResource("theme0.png").toString());
				opponentImage.setImage(new Image(getClass().getClassLoader().getResource("redEnemies ("+en+").png").toString()));
				label1.setStyle("-fx-background-color:red");
				label2.setStyle("-fx-background-color:red");

				

				break;
			case BLUE :
				opponentImage.setImage(new Image(getClass().getClassLoader().getResource("blueEnemies ("+en+").png").toString()));
				label1.setStyle("-fx-background-color:dodgerblue");	
				label2.setStyle("-fx-background-color:dodgerblue");	
				
				

				break;
			case GREEN :
				logoBattleship =  new Image(getClass().getClassLoader().getResource("theme2.png").toString());
				opponentImage.setImage(new Image(getClass().getClassLoader().getResource("greenEnemies ("+en+").png").toString()));
				label1.setStyle("-fx-background-color :rgb(0,255,0) ");
				label2.setStyle("-fx-background-color :rgb(0,255,0) ");

				
				break;
			}
		}



	}
	//		
	//		//set up window to be drag enabled
	//		gamePane.setOnMousePressed(new EventHandler<MouseEvent>() {
	//
	//			@Override
	//			public void handle(MouseEvent event) {
	//				xOffset = primaryWindowStage.getX()- event.getSceneX();
	//				yOffset =  primaryWindowStage.getY() -event.getSceneY();
	//			}
	//
	//			
	//		});
	//		
	//		gamePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
	//
	//			@Override
	//			public void handle(MouseEvent event) {
	//				primaryWindowStage.setX(event.getSceneX() + xOffset);
	//				primaryWindowStage.setY(event.getSceneY() + yOffset);
	//
	//			}
	//
	//			
	//		});
	//
	@FXML
    void dragcruiser(MouseEvent event) {
	        Dragboard db = carrier.startDragAndDrop(TransferMode.MOVE);
	        db.setDragView(createSnapshot(carrier), event.getX(), event.getY());

	        ClipboardContent content = new ClipboardContent();
	        content.putString("");
	        db.setContent(content);
	        event.consume();
	    
    }
	
	private WritableImage createSnapshot(HBox node) {
	    SnapshotParameters snapshotParams = new SnapshotParameters();
	    WritableImage image = node.snapshot(snapshotParams, null);
	    return image;
	}

    @FXML
    void handleExitButtonClick(ActionEvent event) {
    	System.out.println("ThemeExit Button Clicked");
		System.exit(0);
    }

    @FXML
    void handleHomeButtonClick(ActionEvent event) {
    	System.out.println("Theme  HomeButton Clicked");
		//okButton.setText("yay!!");
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("../FirstScene.fxml"));
			Scene scene = new Scene(root,400,900);			
			Main.rootStage.setScene(scene);
			Main.rootStage.setFullScreen(true);
			//primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

	


	public void setButtonBar() {
		System.out.println("Theme  GreenTheme Clicked");
		buttonBarHBox.setStyle("-fx-background-color:dodgerblue");	
		if (Main.colorTheme != null) {
			switch(Main.colorTheme){
			case RED :
				buttonBarHBox.setStyle("-fx-background-color:red");	

				break;
			case BLUE :
				buttonBarHBox.setStyle("-fx-background-color:dodgerblue");	

				break;
			case GREEN :
				buttonBarHBox.setStyle("-fx-background-color :rgb(0,255,0) ");
				break;
			}
		}
	}

	private void computerMove() {
		//choose stacked play or not
		while (computerTurn) { // keep goind or not
			int x = random.nextInt(11-1)+1;
			int y = random.nextInt(11-1)+1;

			ShipPart shipPart = playerBoard.getShipPart(x, y);// get the shipPart at that location
			if (shipPart.shipPartWasHit) /// loop jump next if the sell is unavalable
				continue;

			computerTurn = shipPart.hitShipPart(); //set the shipPart to was shot true

			if (playerBoard.ships == 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("YOU LOSE");
				alert.showAndWait();
				System.out.println("YOU LOSE");

			}
		}
	}

	private void startGame() {
		int computerShips = 5; //set max ship type of enemie

		while (computerShips > 0) {// ship type determined by curr value of enemieShips
			int x = random.nextInt(11-1)+1;
			int y = random.nextInt(11-1)+1;

			MouseButton[] points = new MouseButton[] {MouseButton.PRIMARY,MouseButton.SECONDARY	};
			int ran = random.nextInt(2);
			// place enemy ships
			if (computerBoard.putShipInPlace(new Ship(computerShips, points[ran]), x, y)) {
				computerShips--;
			}
		}

		running = true;
	}


}
