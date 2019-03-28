package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import graphics.UserInterface.ButtonObject;

public class DisplayFrame extends JPanel implements MouseListener {
	private ArrayList<GraphicObject> listOfObjects;
	private ArrayList<ButtonObject> listOfButtons;
	private Animation background;
	private int width, height;
	private Coordinate coords;
	private int mouseX, mouseY;
	private boolean mouseDown;
	private int clicks;
	
	public DisplayFrame(int width, int height, Coordinate coords) {
		this.width = width;
		this.height = height;
		//setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        addMouseListener(this);
      
		//setBackground(Color.getHSBColor(0.18f, 0.27f, 0.89f));
        setBackground(Color.black);
        setForeground(Color.BLACK);
        setFont(new Font("Ariel", Font.PLAIN, 20));
        listOfObjects = new ArrayList<>();
        listOfButtons = new ArrayList<>();
        this.coords = coords;
        clicks = 0;
    }
	
	public DisplayFrame(int width, int height) {
		this(width, height, new Coordinate(0, 0));
    }
	
	public void setBackground(Animation anim) {
		background = anim;
	}
	
	
	public void add(GraphicObject graphicObject) {
		listOfObjects.add(graphicObject);
		System.out.println(graphicObject);
	}
	public void addButton(ButtonObject button) {
		listOfButtons.add(button);
	}
	
	
	public void update(double tick) {
		try {
			for(GraphicObject obj : listOfObjects) {
				obj.update(tick);
			}
		} catch (Exception e) {}
		try {
			for(ButtonObject button : listOfButtons) {
				button.update(tick);
			}
		} catch (Exception e) {}
		
		repaint();
		//System.out.println("Updating");
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(graphics instanceof Graphics2D ) {
        	graphics = (Graphics2D) graphics;
        	((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        graphics.drawString("Mouse x: " + mouseX + "   Mouse y: " + mouseY, 20, 20);
    	graphics.drawString("Mouse Down: " + mouseDown + "   Mouse Clicks: " + clicks, 20, 80);
    	try {
    		graphics.drawImage(background.getFrame(), -1, -3, null);
    	} catch (Exception e) {
    		
    	}
    	for(GraphicObject object : listOfObjects) {
    		try {
    			object.paint(graphics, x(), y());
    		} catch (Exception e) {
    			
    		}
        }
    	for(ButtonObject object : listOfButtons) {
    		try {
    			object.paint(graphics, x(), y());
    		} catch (Exception e) {
    			
    		}
        }
    }
	
	
	public void setMouseCoords(int x, int y) {
		mouseX = x;
		mouseY = y;
	}
	public void setHeight(int h) {
		height = h;
	}
	public void setWidth(int w) {
		width = w;
	}
	
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	private int y() {
		return coords.getX();
	}

	private int x() {
		return coords.getY();
	}
	public ArrayList<GraphicObject> getObjects(){
		return listOfObjects;
	}
	public ArrayList<ButtonObject> getButtons() {
		return listOfButtons;
	}
	
	//Mouse Listener
	@Override
	public void mouseClicked(MouseEvent e) {
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		Point windowPoint = this.getLocation();
		Coordinate mouseCoords = new Coordinate(mousePoint.x - windowPoint.x, mousePoint.y - windowPoint.y);
		try {
			for(ButtonObject button : getButtons()) {
				if(button.pointTouch(mouseCoords)) {
					button.clicked();
				} else {
					button.noContact();
				}
			}
		} catch (Exception e1) {
			
		}
		clicks += 1;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseDown = true;
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		Point windowPoint = this.getLocation();
		Coordinate mouseCoords = new Coordinate(mousePoint.x - windowPoint.x, mousePoint.y - windowPoint.y);
		for(ButtonObject button : getButtons()) {
			if(button.pointTouch(mouseCoords)) {
				button.mouseDown();
			} else {
				button.noContact();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		Point windowPoint = this.getLocation();
		Coordinate mouseCoords = new Coordinate(mousePoint.x - windowPoint.x, mousePoint.y - windowPoint.y);
		try {
			for(ButtonObject button : getButtons()) {
				button.mouseUp();
			}
		} catch (Exception e1) {
			
		}
		mouseDown = false;
	}

	public void clearAll() {
		// TODO Auto-generated method stub
		listOfButtons.clear();
		listOfObjects.clear();
		
	}
	
	public boolean mouseDown() {
		return mouseDown;
	}
}
