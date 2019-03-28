package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GraphicObject {
	protected Coordinate coords;
	protected int[][] points;
	protected double radius; 
	protected int height;
	protected int width;
	
	
	protected Color color;
	protected Animation animation;
	
	/*
	 * Constructs an Object for the graphical userface.
	 *  
	 * @param p convex points with origin of (0, 0)
	 */
	public GraphicObject(Animation anim, Coordinate coords, int width, int height) {
		animation = anim;
		this.coords = coords;
		this.width = width;
		this.height = height;
		color = Color.BLACK;
	}
	
	public void paint(Graphics graphic, int x, int y) {
		try {
			if(animation.getFrame() != null) {
				graphic.drawImage(animation.getFrame(), x() + x, y() + y, null);
			} else {
				graphic.setColor(color);
				graphic.fillRect(x() + x, y() + y, width, height);
			} 
		} catch (Exception e) {
			graphic.setColor(color);
			graphic.fillRect(x() + x, y() + y, width, height);
		}
		
	}
	
	
	public boolean objectTouch(GraphicObject object) {
		if(object.leftX() < this.rightX() && object.rightX() > this.leftX()) {
			if(object.topY() < this.bottomY() && object.bottomY() > this.topY()) 
				return true;
		}
		return false;
	}
	
	public boolean pointTouch(Coordinate coords) {
		if(coords.getX() > leftX() && coords.getX() < rightX()) {
			if(coords.getY() > topY() && coords.getY() < bottomY())
				return true;
		}
		return false;
	}

	public void setCoordinate(int x, int y) {
		coords.setCoords(x, y);
	}
	
	//
	public int rightX() {
		return coords.getX() + width;
	}
	public int leftX() {
		return x();
	}
	public int topY() {
		return y();
	}
	public int bottomY() {
		return y() + height;
	}
	
	//
	public int x() {
		return coords.getX();
	}
	public int y() {
		return coords.getY();
	}
	
	public BufferedImage getFrame() {
		return animation.getFrame();
	}
	
	public String toString() {
		return "Object: " + x() + ", " + y();
	}

}
