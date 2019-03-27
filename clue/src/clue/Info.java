package clue;

import java.awt.Color;

public class Info
{
	private String infoType;
	private String infoName;
	
	private Color color;
	private String info;
	Info ()
	{
		this.color = new Color(238, 238, 238);
		this.info = "UNKNOWN";
	}
	
	public void changeInfo(String s) {
		info = s;
		switch (info) {
			case "CARD":
				color = Color.red;
				break;
			case "KNOWLEDGE":
				color = Color.pink;
				break;
			case "FAKE":
				color = Color.pink;
				break;
			case "MYSTERY":
				color = new Color(238, 238, 238);
				break;
		}
	}
	
	public boolean isCard() {
		if (info.equals("CARD"))
			return true;
		return false;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getString() {
		return info;
	}
}
