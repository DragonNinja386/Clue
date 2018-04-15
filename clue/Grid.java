package clue;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Grid extends JButton
{
	private static final long serialVersionUID = 1L;
	private Grid[] doors;
	private boolean isDoor;
	private int room;
	private int number;
	private int heuristic;
	private int parentX;
	private int parentY;
	private int coordX;
	private int coordY;
	
	protected Grid(String s)
	{
		super(s);
		
    	number = 0;
    	
        setFont(new Font("SanSerif", Font.PLAIN, 10));
        setLayout(null);
        setVisible(true);
        setBorder(null);
        setBorderPainted(true);
        setFocusPainted(false);
	}
	
	protected Grid()
	{
    	number = 0;
    	
        setFont(new Font("SanSerif", Font.PLAIN, 10));
        setLayout(null);
        setVisible(true);
        setBorder(null);
        setBorderPainted(true);
        setFocusPainted(false);
	}

	public boolean checkDoor()
	{
		return isDoor;
	}
	
	public int getRoom()
	{
		return room;
	}
	
    public int getNumber()
	{
		return number;
	}

    public int getH()
    {
    	return heuristic;
    }
    
    public int getCoordX()
    {
    	return coordX;
    }
    
    public int getCoordY()
    {
    	return coordY;
    }
    
    public int getParentX()
    {
    	return parentX;
    }
    
    public int getParentY()
    {
    	return parentY;
    }
	
    public Grid[] getDoors()
	{
		return doors;
	}
    
    public void setDoor(boolean set)
    {
    	this.isDoor = set;
    }
    
    public void setRoom(int r)
    {
    	this.room = r;
    }
    
    public void setCoord(int x, int y)
    {
    	coordX = x;
    	coordY = y;
    }
    
    public void setH(int h)
    {
    	heuristic = h;
    }
    
    public void setParentX(int x)
    {
    	parentX = x;
    }
    
    public void setParentY(int y)
    {
    	parentY = y;
    }
    
    public void setNumber(int n)
    {
    	number = n;
    	if (n != 0) this.setText(Integer.toString(n));
    	else this.setText(" ");
    }
    
	public void setDoors(Grid[] d)
	{
		doors = d;
	}
	
    public String toString()
    {
    	return "";
    }
}
