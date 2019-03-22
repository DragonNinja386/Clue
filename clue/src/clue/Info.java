package clue;

import java.awt.Color;

public enum Info
{
	CARD (Color.red, "CARD"),
	KNOWLEDGE (Color.pink, "KNOWN"),
	MYSTERY (new Color(238, 238, 238), "UNKNOWN");
	
	private Color color;
	private String infoName;
	Info (Color c, String s)
	{
		this.color = c;
		this.infoName = s;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public String getString()
	{
		return infoName;
	}
}
