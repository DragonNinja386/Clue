package graphics.UserInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import graphics.Animation;
import graphics.Coordinate;
import graphics.GraphicObject;

public class ButtonObject extends GraphicObject{
	protected boolean mouseHover;
	protected boolean mouseDown;
	protected boolean hasFocus;
	protected ButtonAction buttonAction;
	protected String textToDisplay;
	protected Font currentFont;
	
	
	public static ArrayList<String> splitString(String text, int i) {
		ArrayList<String> strings = new ArrayList<>();
		int setback = 0;
		int tempSetBack = 0;
		String tempText;
		for(int i1 = 1; i1 < (text.length() / i) + 2; i1++) {
			//System.out.println(text.length() + " " + (i1) * i);
			//System.out.println(text.length() < (i * i1));
			if(text.length() > ((i * i1) - setback)) {
				System.out.println("Not end");
				//tempSetBack = setback;
				//while(text.charAt((i1 * i) - setback) != ' ') {
					//tempSetBack++;
				//}
				
				System.out.println("Char:: " + text.charAt((i1 * i) - setback));
				tempText = text.substring((i1 - 1) * i - setback, ((i1) * i) - tempSetBack);
			} else {
				System.out.println("End");
				tempText = text.substring((i1 - 1) * i - setback, text.length() - 1);
			}
			System.out.println(tempText);
			strings.add(tempText);
		}
		
		return strings;
	}
	
	public ButtonObject (Animation anim, Coordinate coords, int width, int height, ButtonAction action) {
		super(anim, coords, width, height);
		buttonAction = action;
		currentFont = new Font("Showcard gothic", Font.PLAIN, 58);
		hasFocus = true;
	}
	public ButtonObject (Animation anim, Coordinate coords, int width, int height) {
		this(anim, coords, width, height, null);
	}
	
	@Override
	public void update(double tick ) {
		if(mouseDown) {
			animation.setFrame(2);
		} else if (mouseHover) {
			animation.setFrame(1);
			currentFont = new Font("showcard gothic", Font.BOLD, 58);
		} else {
			animation.setFrame(0);
			currentFont = new Font("showcard gothic", Font.PLAIN, 58);
		}
	}
	
	public void setText(String text) {
		textToDisplay = text;
	}
	
	public void setFocus(boolean focused) {
		hasFocus = focused;
	}
	
	
	//Mouse Interaction classes
	public void mouseHover() {
		if(hasFocus) {
			mouseHover = true;
		}
	}	
	
	public void mouseDown() {
		if(hasFocus) {
			mouseDown = true;
		}
	}
	
	public void mouseUp() {
		mouseDown = false;
	}
	
	public void clicked() {
		// TODO Auto-generated method stub
		if(hasFocus) {
			try {
				buttonAction.action();
			} catch(Exception e) {
				System.err.println("No Action Set");
			}
		}
	}
	
	public void noContact() {
		// TODO Auto-generated method stub
		mouseHover = false;
	}
	
	
	public void setAction(ButtonAction action) {
		buttonAction = action;
	}
	
	public void paint(Graphics graphic, int x, int y) {
		try {
			if(animation.getFrame() != null) {
				graphic.drawImage(animation.getFrame(), x + x(), y + y(), null);
			} else {
				graphic.setColor(color);
				graphic.fillRect(x(), y(), width, height);
			} 
		} catch (Exception e) {
			graphic.setColor(color);
			graphic.fillRect(x(), y(), width, height);
		}	
		try {
			graphic.setColor(Color.getHSBColor(0.09f, 0.73f, 0.29f));
			graphic.setFont(currentFont);
			graphic.drawString(textToDisplay, x() + 20, y() + 70);
		} catch (Exception e) {
			
		}
	}
	
	public boolean getMouseDown() {
		return mouseDown;
	}
	
	public boolean isFocused() {
		return hasFocus;
	}
}
