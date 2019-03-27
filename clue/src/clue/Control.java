package clue;

public class Control {
	public int currentPlayer;
	GraphicsControl gc;
	
	public Control() {
		currentPlayer = 1;
		gc = new GraphicsControl();
	}
}
