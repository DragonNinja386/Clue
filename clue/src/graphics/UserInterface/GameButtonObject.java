package graphics.UserInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import graphics.Animation;
import graphics.Coordinate;

public class GameButtonObject extends ButtonObject {

	public GameButtonObject(Animation anim, Coordinate coords, int width, int height) {
		super(anim, coords, width, height);
		// TODO Auto-generated constructor stub
	}

	public void update(double tick ) {
		if(mouseDown) {
			animation.setFrame(2);
		} else if (mouseHover) {
			currentFont = new Font("showcard gothic", Font.PLAIN, 28);
			animation.setFrame(1);

		} else {
			currentFont = new Font("showcard gothic", Font.PLAIN, 28);
			animation.setFrame(0);
		}
	}
	
	public void paint(Graphics graphic, int x, int y) {
		try {
			graphic.setColor(Color.getHSBColor(0.09f, 0.73f, 0.29f));
			graphic.setFont(currentFont);
			if(animation.getFrame() != null) {
				graphic.drawImage(animation.getFrame(), x + x(), y + y(), null);
			} else {
				graphic.fillRect(x(), y(), width, height);
			} 
		} catch (Exception e) {
			graphic.setColor(color);
			graphic.fillRect(x(), y(), width, height);
		}	
		try {
			graphic.drawString(textToDisplay, x() + x + 25, y() + y + 42);
		} catch (Exception e) {
			
		}
	}
}
