package clue;

import java.awt.Color;
import java.util.ArrayList;

public class AI
{
	//TODO add variable for if room was not reached in current turn
	//private static boolean[] aiStates = new boolean[3];
	private static int playerCount;
	private int[] playerWeight = new int[6];
	private int[] weaponWeight = new int[5];
	private int[] locationWeight = new int[9];
	private int destination;
	private int room;
	private int aiState;
	
	protected static void restart(int pc)
	{
		playerCount = pc;
		//for (int i = 0; i < aiStates.length; i++) aiStates[i] = false;
	}
	
	protected AI()
	{
		room = -1;
		int rand = 0;
		while (rand != -1)
		{
			rand = 0;//(int)(Math.random() * aiStates.length);
			switch(rand)
			{
			/*
				case 0: //DeepFind - will isolate info based on held cards
					if (!aiStates[0] && playerCount < 5)
					{
						aiStates[0] = true;
						aiState = 0;
						rand = -1;
					}
					break;
				case 1: //Deception - will prioritize held cards to throw opponents off their trail
					if (!aiStates[1])
					{
						aiStates[1] = true;
						aiState = 1;
						rand = -1;
					}
					break;
			*/
				case 0: //Random - Will randomly make guesses based off info
					//if (!aiStates[2])
					//{
					//	aiStates[2] = true;
						aiState = 0;
						rand = -1;
					//}
					break;
			}
			
			
		}
	}
	
	public int getRoom()
	{
		return room;
	}
	
	public int getDestination()
	{
		return destination;
	}
	
	public int getWeightP()
	{
		return 0;
	}
	
	public int getWeightW()
	{
		return 0;
	}
	
	public int getWeightL()
	{
		return 0;
	}
	
	public void setRoom(int r)
	{
		this.room = r;
	}

	public void setWeight(Info[] p, Info[] w, Info[] l)
	{
		switch (aiState)
		{
		/*
			case 0: //DeepFind - will isolate info based on held cards

				break;
			case 1: //Deception - will prioritize held cards to throw opponents off their trail
		 
				break;
		*/
			case 0: //Random - Will randomly make guesses based off info
				
				break;
		}
	}

	public void setDestination()
	{
		ArrayList<Integer> selection = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++)
			for (int k = 0; k < locationWeight[i]; k++)
				selection.add(i);
		this.destination = selection.get((int)(Math.random() * selection.size()));
	}
}
