package application;

import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipPart extends Rectangle {// each square in board is represented by cell thus extends rectangle
	public int x, y; //position of ship within board
	public Ship ship = null; //part of ship  or whole ship that is contained in cell (if not partc ship then null)
	public boolean shipPartWasHit = false; // if cell was ever shot
	public String label = null;
	private Board board; //reference to the board that this cell isa child of

	public ShipPart(int x, int y, Board board) { //cell constructed with x,y coordinates
		super(60, 55);
		//super(40,40); // call super constructor of rectangle class with  width,length
		this.x = x;
		this.y = y;
		this.board = board;
		BoxBlur blur = new BoxBlur();
		setEffect(blur);
		setFill(Color.DODGERBLUE);
		setStroke(Color.WHITE);
		if (Main.colorTheme != null) {
			switch(Main.colorTheme){
			case RED :

				setFill(Color.RED);
				setStroke(Color.WHITE);
				break;
			case BLUE :
				setFill(Color.DODGERBLUE);
				setStroke(Color.WHITE);
				break;
			case GREEN :
				//-fx-background-color :rgb(0,255,0) 
				setFill(Color.rgb(0, 255, 0));
				setStroke(Color.WHITE);
				break;
			}
		}
	}

	public boolean hitShipPart() {//cell has only one method which is shoot
		shipPartWasHit = true;  //set shot information to true
		// change color to white
		setFill(Color.WHITE);
		setStroke(Color.AQUA);


		if (ship != null) { // if there is a ship or any part of a ship touching this cell  
			ship.attack();//reduce ship health
			setFill(Color.RED); // make this cell red
			if (Main.colorTheme != null) {
				switch(Main.colorTheme){
				case RED :
					setFill(Color.BLACK);
					setStroke(Color.WHITE);
					break;
				}
			}
			if (!ship.isAlive()) { //if the ship health is <= 0
				board.ships--;  //reduce number of ship for this board 
			}
			return true; //return for hit the ship
		}

		return false; // false for a miss
	}
}