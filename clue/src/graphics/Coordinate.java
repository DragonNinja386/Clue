package graphics;

public class Coordinate {
	private int posX;
	private int posY;
	
	public Coordinate(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public void setCoords(int x, int y) {
		posX = x;
		posY = y;	
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public String toString() {
		return "(" + posX + ", " + posY + ")";
	}
}
