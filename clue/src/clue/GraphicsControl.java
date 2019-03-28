package clue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import graphics.Board;
import graphics.ClueWindow;

public class GraphicsControl {
	private static ClueWindow mainFrame;
	private Board board;
	
	public GraphicsControl() {
		mainFrame = new ClueWindow(700, 600);
	}
	
	
}
