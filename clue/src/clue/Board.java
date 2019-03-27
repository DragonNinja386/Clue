package clue;

import javax.swing.JPanel;

public class Board extends JPanel {
	private Grid[][] grid;
	
	public Board() {
		grid = new Grid[13][13];
	}
}
