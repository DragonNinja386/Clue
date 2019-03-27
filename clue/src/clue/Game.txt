package clue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Game
{
	//Global variables
	private static JFrame mainFrame = new JFrame();
	
	private static final String[] LOCATION =
		{"Study", "Library", "Conservatory", "Hall", "Billard Room", "Ballroom", "Lounge", "Dining Room", "Kitchen"};
	private static final String[] WEAPON =
		{"Candlestick", "Knife", "Wrench", "Revolver", "Rope"};
	private static final String[] PERSON =
		{"Colonel Mustard", "Mrs. White", "Professor Plum", "Mrs. Peacock", "Mr. Green", "Mrs. Scarlet"};
	private static AI[] ai;
	private static Grid[][] grid = new Grid[13][13];
	private static Grid[] roomB = new Grid[9];
	private static Info[][] infoP = new Info[6][6];
	private static Info[][] infoW = new Info[6][5];
	private static Info[][] infoL = new Info[6][9];
	private static boolean[] loosers;
	private static int[] proveFail;
	private static int[][] room = new int[9][2];
	private static int[][] coord = new int[6][2];
	private static int accuseP, accuseW, accuseL;
	private static int guessP, guessW, guessL;
	private static int players, coms, playerCount;
	private static int move;
	private static boolean guessed;
	
	//Initializes main frame
	public static void main(String[] args)
	{
		try //Sets the UI to match the systems UI
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
		catch (Exception e)
		{
			System.out.println("Could not load System look");
		}
		//Initialize game window frame
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(700, 600);
		mainFrame.setResizable(false);
		mainFrame.setLayout(null);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.setTitle("Clue");
		
		start("players");
	}
	
	//Initializes the game
	private static void start(String process)
	{
		JPanel startP = new JPanel();
		startP.setLayout(null);
		mainFrame.setContentPane(startP);
		switch (process)
		{
			//Gets user to set num of human players
			case "players":
				JLabel playerL = new JLabel("How many human players?");
				playerL.setBounds(275, 75, 200, 30);
				startP.add(playerL);
				
				JButton[] playerNum = new JButton[6];
				for (int i = 0; i < playerNum.length; i++)
				{
					playerNum[i] = new JButton(Integer.toString(i + 1));
					playerNum[i].setBounds(40 + 100 * i, 500, 60, 30);
					playerNum[i].addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							players = Integer.parseInt(e.getActionCommand());
							if (players == 6)
							{
								coms = 0;
								start("set");
							}
							else
							{
								start("coms");
							}
						}
					});
					startP.add(playerNum[i]);
				}
				break;
			
			//Gets player to set num of computers
			case "coms":
				JLabel computerL = new JLabel("How many computer players? (Not implemented)"); //TODO
				computerL.setBounds(275, 75, 400, 30);
				startP.add(computerL);
				
				//Prevents player from choosing to have only 1 player in total
				if (players == 1)
				{
					JButton[] comsNum = new JButton[5];
					for (int i = 0; i < comsNum.length; i++)
					{
						comsNum[i] = new JButton(Integer.toString(i + 1));
						comsNum[i].setBounds(40 + 100 * i, 500, 60, 30);
						comsNum[i].addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								coms = Integer.parseInt(e.getActionCommand());
								start("set");
							}
						});
						startP.add(comsNum[i]);
					}
				}
				else //Allows player to choose to have no computers
				{
					JButton[] comsNum = new JButton[7 - players];
					for (int i = 0; i < comsNum.length; i++)
					{
						comsNum[i] = new JButton(Integer.toString(i));
						comsNum[i].setBounds(40 + 100 * i, 500, 60, 30);
						comsNum[i].addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								coms = Integer.parseInt(e.getActionCommand());
								start("set");
							}
						});
						startP.add(comsNum[i]);
					}
				}
				break;
			
			//"Hands out cards" to players and computers
			case "set":
				playerCount = coms + players;
				infoP = new Info[players + coms][6];
				infoW = new Info[players + coms][5];
				infoL = new Info[players + coms][9];
				
				//Sets final cards to the side
				accuseP = (int)(Math.random() * 6);
				accuseW = (int)(Math.random() * 5);
				accuseL = (int)(Math.random() * 9);
				
				//sets cards to hand out
				int cpp = 17 / playerCount; //Cards per player
				int remainder = 17 % playerCount; //Left over cards
				
				//Sets all info to false
				for (int i = 0; i < 6; i++)
					for (int x = 0; x < playerCount; x++)
						infoP[x][i] = Info.MYSTERY;
				for (int i = 0; i < 5; i++)
					for (int x = 0; x < playerCount; x++)
						infoW[x][i] = Info.MYSTERY;
				for (int i = 0; i < 9; i++)
					for (int x = 0; x < playerCount; x++)
						infoL[x][i] = Info.MYSTERY;
				
				//Sets info from "handed" out cards to true
				for (int i = 0; i < playerCount; i++)
					for (int x = 0; x < cpp; x++)
						while (true)
						{
							int card = (int)(Math.random() * 2) + 1;
							if (card == 1)
							{
								boolean go = true;
								card = (int)(Math.random() * 6);
								
								for (int y = 0; y < playerCount; y++)
									if (infoP[y][card] == Info.CARD || accuseP == card)
									{
										go = false;
										break;
									}
								if (go == true)
								{
									infoP[i][card] = Info.CARD;
									break;
								}
							}
							else if (card == 2)
							{
								boolean go = true;
								card = (int)(Math.random() * 5);
								for (int y = 0; y < playerCount; y++)
									if (infoW[y][card] == Info.CARD || accuseW == card)
									{
										go = false;
										break;
									}
								if (go == true)
								{
									infoW[i][card] = Info.CARD;
									break;
								}
							}	
							else if (card == 3);
							{
								boolean go = true;
								card = (int)(Math.random() * 9);
								for (int y = 0; y < playerCount; y++)
									if (infoL[y][card] == Info.CARD || accuseL == card)
									{
										go = false;
										break;
									}
								if (go == true)
								{
									infoL[i][card] = Info.CARD;
									break;
								}
							}
						}
				for (int i = 0; i < remainder; i++)
					while (true)
					{
						int card = (int)(Math.random() * 2) + 1;
						if (card == 1)
						{
							boolean go = true;
							card = (int)(Math.random() * 6);
							for (int y = 0; y < playerCount; y++)
								if (infoP[y][card] == Info.CARD || accuseP == card)
								{
									go = false;
									break;
								}
							if (go == true)
							{
								infoP[i][card] = Info.CARD;
								break;
							}
						}
						else if (card == 2)
						{
							boolean go = true;
							card = (int)(Math.random() * 5);
							for (int y = 0; y < playerCount; y++)
								if (infoW[y][card] == Info.CARD || accuseW == card)
								{
									go = false;
									break;
								}
							if (go == true)
							{
								infoW[i][card] = Info.CARD;
								break;
							}
						}	
						else if (card == 3);
						{
							boolean go = true;
							card = (int)(Math.random() * 9);
							for (int y = 0; y < playerCount; y++)
								if (infoL[y][card] == Info.CARD || accuseL == card)
								{
									go = false;
									break;
								}
							if (go == true)
							{
								infoL[i][card] = Info.CARD;
								break;
							}
						}
					}
				
				ai = new AI[coms];
				for (int i = 0; i < ai.length; i++)
				{
					ai[i] = new AI();
					ai[i].setWeight(infoP[i], infoW[i], infoL[i]);
				}
				
				loosers = new boolean[playerCount];
				for (int i = 0; i < loosers.length; i++)
				{
					loosers[i] = false;
				}
				
				proveFail = new int[playerCount];
				for (int i = 0; i < loosers.length; i++)
				{
					proveFail[i] = 0;
				}
				
				start("setgrid");
				break;
			
			//Initializes grid
			case "setgrid":
				//Initializes walking spaces
				for (int x = 0; x < 13; x++)
					for (int y = 0; y < 13; y++)
						if (x == 3 || x == 4 || x == 8 || x == 9)
						{
							grid[x][y] = new Grid(" ");
							grid[x][y].setDoor(false);
							grid[x][y].setBounds(x * 31 + 20, y * 31 + 30, 30, 30);
							grid[x][y].setCoord(x, y);
						}
						else if (y == 3 || y == 4 || y == 8 || y == 9)
						{
							grid[x][y] = new Grid(" ");
							grid[x][y].setDoor(false);
							grid[x][y].setBounds(x * 31 + 20, y * 31 + 30, 30, 30);
							grid[x][y].setCoord(x, y);
						}
				
				//Initializes door spaces
				//Room 0
				grid[1][3].setDoor(true);
				grid[1][3].setRoom(0);
				grid[1][3].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[3][1].setDoor(true);
				grid[3][1].setRoom(0);
				grid[3][1].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 1
				grid[2][4].setDoor(true);
				grid[2][4].setRoom(1);
				grid[2][4].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[2][8].setDoor(true);
				grid[2][8].setRoom(1);
				grid[2][8].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 2
				grid[1][9].setDoor(true);
				grid[1][9].setRoom(2);
				grid[1][9].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[3][11].setDoor(true);
				grid[3][11].setRoom(2);
				grid[3][11].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 3
				grid[4][1].setDoor(true);
				grid[4][1].setRoom(3);
				grid[4][1].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[6][3].setDoor(true);
				grid[6][3].setRoom(3);
				grid[6][3].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[8][1].setDoor(true);
				grid[8][1].setRoom(3);
				grid[8][1].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 4
				grid[6][4].setDoor(true);
				grid[6][4].setRoom(4);
				grid[6][4].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[6][8].setDoor(true);
				grid[6][8].setRoom(4);
				grid[6][8].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[4][6].setDoor(true);
				grid[4][6].setRoom(4);
				grid[4][6].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[8][6].setDoor(true);
				grid[8][6].setRoom(4);
				grid[8][6].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 5
				grid[5][9].setDoor(true);
				grid[5][9].setRoom(5);
				grid[5][9].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[8][10].setDoor(true);
				grid[8][10].setRoom(5);
				grid[8][10].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 6
				grid[9][2].setDoor(true);
				grid[9][2].setRoom(6);
				grid[9][2].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[10][3].setDoor(true);
				grid[10][3].setRoom(6);
				grid[10][3].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 7
				grid[9][5].setDoor(true);
				grid[9][5].setRoom(7);
				grid[9][5].setBorder(BorderFactory.createLineBorder(Color.black));
				grid[11][8].setDoor(true);
				grid[11][8].setRoom(7);
				grid[11][8].setBorder(BorderFactory.createLineBorder(Color.black));
				//Room 8
				grid[11][9].setDoor(true);
				grid[11][9].setRoom(8);
				grid[11][9].setBorder(BorderFactory.createLineBorder(Color.black));
				
				//Initialize room spaces
				for (int x = 0; x < 3; x++)
					for (int y = 0; y < 3; y++)
					{
						room[x * 3 + y][0] = x * 5 + 1;
						room[x * 3 + y][1] = y * 5 + 1;
						roomB[x * 3 + y] = new Grid();
						roomB[x * 3 + y].setName("");
						roomB[x * 3 + y].setDoor(false);
						roomB[x * 3 + y].setBounds(x * 155 + 20, y * 155 + 30, 92, 92);
						roomB[x * 3 + y].setCoord(1 + x * 5, 1 + y * 5);
					}
				
				roomB[0].setName("Study");
				roomB[0].setRoom(0);
				roomB[0].setDoors(new Grid[] {grid[1][3], grid[3][1]});
				roomB[1].setName("Library");
				roomB[1].setRoom(1);
				roomB[1].setDoors(new Grid[] {grid[2][4], grid[2][8]});
				roomB[2].setName("Conservatory");
				roomB[2].setRoom(2);
				roomB[2].setDoors(new Grid[] {grid[1][9], grid[3][11]});
				roomB[3].setName("Hall");
				roomB[3].setRoom(3);
				roomB[3].setDoors(new Grid[] {grid[4][1], grid[6][3], grid[8][1]});
				roomB[4].setName("Billard Room");
				roomB[4].setRoom(4);
				roomB[4].setDoors(new Grid[] {grid[6][4], grid[6][8], grid[4][6], grid[8][6]});
				roomB[5].setName("Ballroom");
				roomB[5].setRoom(5);
				roomB[5].setDoors(new Grid[] {grid[5][9], grid[8][10]});
				roomB[6].setName("Lounge");
				roomB[6].setRoom(6);
				roomB[6].setDoors(new Grid[] {grid[9][2], grid[10][3]});
				roomB[7].setName("Dining Room");
				roomB[7].setRoom(7);
				roomB[7].setDoors(new Grid[] {grid[9][5], grid[11][8]});
				roomB[8].setName("Kitchen");
				roomB[8].setRoom(8);
				roomB[8].setDoors(new Grid[] {grid[11][9]});
				
				//Initializes player positions
				for (int i = 1; i <= playerCount; i++)
					while(true)
					{
						int location = (int)(Math.random() * 4) + 1;
						if (location == 1) // X = 0
						{
							location = (int)(Math.random() * 13);
							if (grid[0][location] != null && grid[0][location].getNumber() == 0)
							{
								coord[i-1][0] = 0;
								coord[i-1][1] = location;
								grid[0][location].setNumber(i);
								break;
							}
						}
						else if (location == 2) // X = 12
						{
							location = (int)(Math.random() * 13);
							if (grid[12][location] != null && grid[12][location].getNumber() == 0)
							{
								coord[i-1][0] = 12;
								coord[i-1][1] = location;
								grid[12][location].setNumber(i);
								break;
							}
						}
						else if (location == 3) // Y = 0
						{
							location = (int)(Math.random() * 13);
							if (grid[location][0] != null && grid[location][0].getNumber() == 0)
							{
								coord[i-1][0] = location;
								coord[i-1][1] = 0;
								grid[location][0].setNumber(i);
								break;
							}
						}
						else // Y = 12
						{
							location = (int)(Math.random() * 13);
							if (grid[location][12] != null && grid[location][12].getNumber() == 0)
							{
								coord[i-1][0] = location;
								coord[i-1][1] = 12;
								grid[location][12].setNumber(i);
								break;
							}
						}
					}
				move = (int)(Math.random() * 6) + (int)(Math.random() * 6) + 2;
				guessed = false;
				switchPlayer(-1);
				break;
		}
		startP.revalidate();
		startP.repaint();
	}
	
	//Switches from one player to another
	private static void switchPlayer(int player)
	{
		move = (int)(Math.random() * 6) + (int)(Math.random() * 6) + 2;
		if (++player == playerCount)
			player = 0;
		int newPlayer = player;
		
		JPanel switchJP = new JPanel();
		switchJP.setLayout(null);
		
		JLabel switchL = new JLabel("Player: " + (player + 1) + " (" + PERSON[player] + "), it is now your turn");
		switchL.setBounds(150, 200, 300, 30);
		switchJP.add(switchL);
		
		JButton continueB = new JButton("CONTINUE");
		continueB.setBounds(500, 500, 100, 30);
		continueB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				turnPlayer(newPlayer);
			}
		});
		switchJP.add(continueB);
		
		if (player < players)
			mainFrame.setContentPane(switchJP);
		else
			turnCom(player);
		
		switchJP.repaint();
		switchJP.revalidate();
		
		if (loosers[player])
			switchPlayer(player);
	}
	
	//Main game loops
	private static void turnPlayer(int player)
	{
		/* TODO List
		 * Fix bug: 1 player left, proofing player who was eliminated / guessing still causes red flags
		 * Add "secret passages"
		 * Online functionality?
		 */
		int playerX = coord[player][0];
		int playerY = coord[player][1];
		
		JPanel turnP = new JPanel();
		turnP.setLayout(null);
		updateGrid(turnP);
		mainFrame.setContentPane(turnP);
		
		JLabel moveL = new JLabel("MOVES: " + move);
		moveL.setBounds(53, 475, 150, 20);
		turnP.add(moveL);
		
		JLabel playerL = new JLabel("PLAYER: " + (player + 1) + " (" + PERSON[player] + ")");
		playerL.setBounds(21, 5, 200, 20);
		turnP.add(playerL);
		
		if (move > 0)
		{
	    	//Sets nearby spaces for player to move to
			//Move Up
	    	if (playerY != 0 && grid[playerX][playerY-1] != null && grid[playerX][playerY-1].getText().equals(" "))
	    	{
	    		grid[playerX][playerY-1].setBackground(Color.red);
	    		grid[playerX][playerY-1].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						move(player, playerX, playerY - 1);
						turnPlayer(player);
					}
				});
	    	}
	    	//Move Down
	    	if (playerY != 12 && grid[playerX][playerY+1] != null && grid[playerX][playerY+1].getText().equals(" "))
	    	{
	    		grid[playerX][playerY+1].setBackground(Color.red);
	    		grid[playerX][playerY+1].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						move(player, playerX, playerY + 1);
						turnPlayer(player);
					}
				});
	    	}
	    	//Move Left
	    	if (playerX != 0 && grid[playerX-1][playerY] != null && grid[playerX-1][playerY].getText().equals(" "))
	    	{
	    		grid[playerX-1][playerY].setBackground(Color.red);
	    		grid[playerX-1][playerY].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						move(player, playerX - 1, playerY);
						turnPlayer(player);
					}
				});
	    	}
	    	//Move Right
	    	if (playerX != 12 && grid[playerX+1][playerY] != null && grid[playerX+1][playerY].getText().equals(" "))
	    	{
	    		grid[playerX+1][playerY].setBackground(Color.red);
	    		grid[playerX+1][playerY].addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						move(player, playerX + 1, playerY);
						turnPlayer(player);
					}
				});
	    	}
	    	//Displays room as red if in a room
	    	for (int i = 0; i < 9; i++)
	    		if (roomB[i].getText().contains(Integer.toString(player + 1)))
	    			roomB[i].setBackground(Color.red);
	    	
	    	//Allows players to use a door to move into a room
			if (grid[playerX][playerY] != null && grid[playerX][playerY].checkDoor())
			{
				JButton enterB = new JButton("ENTER");
				enterB.setBounds(200, 500, 100, 30);
				enterB.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						moveRoom(player, grid[playerX][playerY].getRoom(), playerX, playerY);
						turnPlayer(player);
					}
				});
				turnP.add(enterB);
			}
			
			//Allows players to move out of rooms and onto doors
			for (int i = 0; i < room.length; i++)
			{
				if (playerX == room[i][0] && playerY == room[i][1])
				{
					for (Grid g : roomB)
						if (g.getName().contains(Integer.toString(player)))
						{
							Grid[] selectedRooms = g.getDoors();
							for (Grid gr : selectedRooms)
							{
								gr.setBackground(Color.red);
								gr.addActionListener(new ActionListener()
								{
									@Override
									public void actionPerformed(ActionEvent e)
									{
										move(player, gr.getCoordX(), gr.getCoordY());
										g.setName(g.getName().substring(0, g.getName().indexOf(Integer.toString(player))) + g.getName().substring(g.getName().indexOf(Integer.toString(player)) + 1));
										turnPlayer(player);
									}
								});
							}
							break;
						}
				}
			}
		}
		
		//Allows player to make guess if in room
		for (int i = 0; i < room.length; i++)
		{
			if (playerX == room[i][0] && playerY == room[i][1])
			{
				//Allows player to make a guess
				if (!guessed)
				{
					int roomNum = i;
					JButton guessB = new JButton("GUESS");
					guessB.setBounds(350, 500, 100, 30);
					guessB.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							guess(player, roomNum);
						}
					});
					turnP.add(guessB);
				}
				break;
			}
		}
		
		//Gives player option to end turn
		JButton endB = new JButton("END");
		endB.setBounds(50, 500, 100, 30);
		endB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				guessed = false;
				switchPlayer(player);
			}
		});
		turnP.add(endB);
		
		//Gives player option to make accusation
		JButton accuseB = new JButton("ACCUSE");
		accuseB.setBounds(500, 500, 100, 30);
		accuseB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				accuse(player);
			}
		});
		turnP.add(accuseB);
		
		JLabel[][] infoTable = new JLabel[2][20];
		for (int i = 0; i < 20; i++)
		{
			infoTable[0][i] = new JLabel();
			infoTable[0][i].setVisible(false);
			turnP.add(infoTable[0][i]);
			infoTable[1][i] = new JLabel();
			infoTable[1][i].setVisible(false);
			turnP.add(infoTable[1][i]);
			
			if (i < 6)
			{
				infoTable[0][i].setBounds(440, 70 + i * 18, 80, 18);
				infoTable[0][i].setText(PERSON[i]);
				
				infoTable[1][i].setBounds(550, 70 + i * 18, 80, 18);
				infoTable[1][i].setText(infoP[player][i].toString());
				infoTable[1][i].setForeground(infoP[player][i].getColor());
			}
			else if (i < 11)
			{
				infoTable[0][i].setBounds(440, 85 + i * 18, 80, 18);
				infoTable[0][i].setText(WEAPON[i - 6]);
				
				infoTable[1][i].setBounds(550, 85 + i * 18, 80, 18);
				infoTable[1][i].setText(infoW[player][i - 6].toString());
				infoTable[1][i].setForeground(infoW[player][i - 6].getColor());
			}
			else
			{
				infoTable[0][i].setBounds(440, 100 + i * 18, 80, 18);
				infoTable[0][i].setText(LOCATION[i - 11]);
				
				infoTable[1][i].setBounds(550, 100 + i * 18, 80, 18);
				infoTable[1][i].setText(infoL[player][i - 11].toString());
				infoTable[1][i].setForeground(infoL[player][i - 11].getColor());
			}
		}
		
		JButton infoB = new JButton("INFO");
		infoB.setBounds(500, 30, 100, 30);
		infoB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (int x = 0; x < 2; x++)
					for (int y = 0; y < 20; y++)	
						if (infoTable[x][y].isVisible()) infoTable[x][y].setVisible(false);
						else infoTable[x][y].setVisible(true);
				
				turnP.revalidate();
		    	turnP.repaint();
			}
		});
		turnP.add(infoB);
		
		
    	turnP.revalidate();
    	turnP.repaint();
	}
	
	private static void turnCom(int player)
	{
		/*	TODO
		 *  Possible bug: surrounded AI
		 *  Simplify destination finding
		 * 	Add checks for door to allow passage through rooms
		 *  Checks for if destination is blocked
		 */
		
		int comPlayer = player - players;
		move = (int)(Math.random() * 6) + (int)(Math.random() * 6) + 2;
		Grid[] destination;
		Grid[] start;
		ArrayList<ArrayList<Grid>> path = new ArrayList<>();
		
		//Set Destination
		ai[comPlayer].setDestination();
		
		//Select possible destinations and start points
		switch (ai[comPlayer].getDestination())
		{
			case 0:
				destination = new Grid[2];
				destination[0] = grid[1][3];
				destination[1] = grid[3][1];
				break;
			case 1:
				destination = new Grid[2];
				destination[0] = grid[2][4];
				destination[1] = grid[2][8];
				break;
			case 2:
				destination = new Grid[2];
				destination[0] = grid[1][9];
				destination[1] = grid[3][11];
				break;
			case 3:
				destination = new Grid[3];
				destination[0] = grid[4][1];
				destination[1] = grid[6][3];
				destination[2] = grid[8][1];
				break;
			case 4:
				destination = new Grid[4];
				destination[0] = grid[6][4];
				destination[1] = grid[6][8];
				destination[2] = grid[4][6];
				destination[3] = grid[8][6];
				break;
			case 5:
				destination = new Grid[2];
				destination[0] = grid[5][9];
				destination[1] = grid[8][10];
				break;
			case 6:
				destination = new Grid[2];
				destination[0] = grid[9][2];
				destination[1] = grid[10][3];
				break;
			case 7:
				destination = new Grid[2];
				destination[0] = grid[9][5];
				destination[1] = grid[11][8];
				break;
			case 8:
				destination = new Grid[1];
				destination[0] = grid[11][9];
				break;
			default:
				destination = new Grid[0];
				System.out.println("If you see this, the AI imploded. Please safely exit the program, thank you.");
				break;
		}
		if (ai[comPlayer].getRoom() == -1)
		{
			 start = new Grid[1];
			 start[0] = grid[coord[player][0]][coord[player][1]];
		}
		else
			start = roomB[ai[comPlayer].getRoom()].getDoors();
		
		//Set path to destination room, regardless of if destination point is blocked
		setPath(player, path, start, destination[0], false);
		
		//Set path to initial destination
		for (Grid d : destination)
			setPath(player, path, start, d, true);

		//Set path to adjacent rooms
		switch (ai[comPlayer].getDestination())
		{
			case 0:
				destination = new Grid[5];
				destination[0] = grid[2][4];
				destination[1] = grid[2][8];
				destination[2] = grid[4][1];
				destination[3] = grid[6][3];
				destination[4] = grid[8][1];
				break;
			case 1:
				destination = new Grid[4];
				destination[0] = grid[1][3];
				destination[1] = grid[3][1];
				destination[2] = grid[1][9];
				destination[3] = grid[3][11];
				break;
			case 2:
				destination = new Grid[4];
				destination[0] = grid[2][4];
				destination[1] = grid[2][8];
				destination[2] = grid[5][9];
				destination[3] = grid[8][10];
				break;
			case 3:
				destination = new Grid[4];
				destination[0] = grid[1][3];
				destination[1] = grid[3][1];
				destination[2] = grid[9][2];
				destination[3] = grid[10][3];
				break;
			case 4:
				destination = new Grid[4];
				destination[0] = grid[6][4];
				destination[1] = grid[6][8];
				destination[2] = grid[4][6];
				destination[3] = grid[8][6];
				break;
			case 5:
				destination = new Grid[3];
				destination[0] = grid[1][9];
				destination[1] = grid[3][11];
				destination[2] = grid[11][9];
				break;
			case 6:
				destination = new Grid[5];
				destination[0] = grid[4][1];
				destination[1] = grid[6][3];
				destination[2] = grid[8][1];
				destination[3] = grid[9][5];
				destination[4] = grid[11][8];
				break;
			case 7:
				destination = new Grid[3];
				destination[0] = grid[9][2];
				destination[1] = grid[10][3];
				destination[2] = grid[11][9];
				break;
			case 8:
				destination = new Grid[4];
				destination[0] = grid[9][5];
				destination[1] = grid[11][8];
				destination[2] = grid[5][9];
				destination[3] = grid[8][10];
				break;
			default:
				destination = new Grid[0];
				System.out.println("If you see this, the AI imploded. Please safely exit the program, thank you.");
				break;
		}
		for (Grid d : destination)
			setPath(player, path, start, d, true);
		
		//Set path to middle room
		destination = new Grid[] { grid[6][4], grid[6][8], grid[4][6], grid[8][6]};
		for (Grid d : destination)
			setPath(player, path, start, d, true);
		

		ArrayList<Grid> chosenPath = path.get(0);
		for (ArrayList<Grid> pathList : path)
		{
			System.out.println("Ran");
			System.out.println(pathList.size());
			System.out.println(move);
			if (pathList.size() + 1 < move)
			{
				chosenPath = pathList;
				break;
			}
		}
		System.out.println("Moves: " + move);
		System.out.println("Path Size: " + chosenPath.size());
		
		System.out.println("Room #: " + (ai[comPlayer].getDestination() + 1));
		moveAI(player, chosenPath);
	}
	
	//Calculates path for AI to follow using an A* algorithm
	private static void setPath(int player, ArrayList<ArrayList<Grid>> path, Grid[] start, Grid destination, boolean flag)
	{
		ArrayList<ArrayList<Grid>> tempPath = new ArrayList<ArrayList<Grid>>();
		ArrayList<Grid> open;
		ArrayList<Grid> closed;
		boolean destinationFound = false;

		for (int x = 0; x < 13; x++)
			for (int y = 0; y < 13; y++)
				if (grid[x][y] != null)
					grid[x][y].setH(Math.abs(x + y - (destination.getCoordX() + destination.getCoordY())));
			
		for (int p = 0; p < start.length; p++)
		{
			open = new ArrayList<Grid>();
			closed = new ArrayList<Grid>();
			closed.add(grid[start[p].getCoordX()][start[p].getCoordY()]);
			if (start[p].getCoordX() + 1 < 13 && grid[start[p].getCoordX() + 1][start[p].getCoordY()] != null && grid[start[p].getCoordX() + 1][start[p].getCoordY()].getText().equals(" "))
				open.add(grid[start[p].getCoordX() + 1][start[p].getCoordY()]);
			if (start[p].getCoordX() - 1 >= 0 && grid[start[p].getCoordX() - 1][start[p].getCoordY()] != null && grid[start[p].getCoordX() - 1][start[p].getCoordY()].getText().equals(" "))
				open.add(grid[start[p].getCoordX() - 1][start[p].getCoordY()]);
			if (start[p].getCoordY() + 1 < 13 && grid[start[p].getCoordX()][start[p].getCoordY() + 1] != null && grid[start[p].getCoordX()][start[p].getCoordY() + 1].getText().equals(" "))
				open.add(grid[start[p].getCoordX()][start[p].getCoordY() + 1]);
			if (start[p].getCoordY() - 1 >= 0 && grid[start[p].getCoordX()][start[p].getCoordY() - 1] != null && grid[start[p].getCoordX()][start[p].getCoordY() - 1].getText().equals(" "))
				open.add(grid[start[p].getCoordX()][start[p].getCoordY() - 1]);
			for (int i = 0; i < open.size(); i++)
			{
				open.get(i).setParentX(start[p].getCoordX());
				open.get(i).setParentY(start[p].getCoordY());
			}
			
			while (true)
			{
				Grid selected = null;
				
				if (open.isEmpty()) //Prevents endless loop / OOB error
					if (flag)
						break;
					else
					{
						ArrayList<Grid> frontier = new ArrayList<>();
						if (destination.getCoordX() + 1 < 13 && grid[destination.getCoordX() + 1][destination.getCoordY()] != null)
							if (closed.contains(grid[destination.getCoordX() + 1][destination.getCoordY()]))
								selected = grid[destination.getCoordX() + 1][destination.getCoordY()];
							else
								frontier.add(grid[destination.getCoordX() + 1][destination.getCoordY()]);
						if (destination.getCoordX() - 1 >= 0 && grid[destination.getCoordX() - 1][destination.getCoordY()] != null)
							if (closed.contains(grid[destination.getCoordX() - 1][destination.getCoordY()]))
								selected = grid[destination.getCoordX() - 1][destination.getCoordY()];
							else
								frontier.add(grid[destination.getCoordX() - 1][destination.getCoordY()]);
						if (destination.getCoordY() + 1 < 13 && grid[destination.getCoordX()][destination.getCoordY() + 1] != null)
							if (closed.contains(grid[destination.getCoordX()][destination.getCoordY() + 1]))
								selected = grid[destination.getCoordX()][destination.getCoordY() + 1];
							else
								frontier.add(grid[destination.getCoordX()][destination.getCoordY() + 1]);
						if (destination.getCoordY() - 1 >= 0 && grid[destination.getCoordX()][destination.getCoordY() - 1] != null)
							if (closed.contains(grid[destination.getCoordX()][destination.getCoordY() - 1]))
								selected = grid[destination.getCoordX()][destination.getCoordY() - 1];
							else
								frontier.add(grid[destination.getCoordX()][destination.getCoordY() - 1]);
						while (selected != null)
						{
							if (frontier.get(0).getCoordX() + 1 < 13 && grid[frontier.get(0).getCoordX() + 1][frontier.get(0).getCoordY()] != null && !frontier.contains(grid[frontier.get(0).getCoordX() + 1][frontier.get(0).getCoordY()]))
								if (closed.contains(grid[frontier.get(0).getCoordX() + 1][frontier.get(0).getCoordY()]))
								{
									selected = grid[frontier.get(0).getCoordX() + 1][frontier.get(0).getCoordY()];
									break;
								}
								else
									frontier.add(grid[frontier.get(0).getCoordX() + 1][frontier.get(0).getCoordY()]);
							if (frontier.get(0).getCoordX() - 1 >= 0 && grid[frontier.get(0).getCoordX() - 1][frontier.get(0).getCoordY()] != null && !frontier.contains(grid[frontier.get(0).getCoordX() - 1][frontier.get(0).getCoordY()]))
								if (closed.contains(grid[frontier.get(0).getCoordX() - 1][frontier.get(0).getCoordY()]))
								{
									selected = grid[frontier.get(0).getCoordX() - 1][frontier.get(0).getCoordY()];
									break;
								}
								else
									frontier.add(grid[frontier.get(0).getCoordX() - 1][frontier.get(0).getCoordY()]);
							if (frontier.get(0).getCoordY() + 1 < 13 && grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() + 1] != null && !frontier.contains(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() + 1]))
								if (closed.contains(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() + 1]))
								{
									selected = grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() + 1];
									break;
								}
								else
									frontier.add(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() + 1]);
							if (frontier.get(0).getCoordY() - 1 >= 0 && grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() - 1] != null && !frontier.contains(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() - 1]))
								if (closed.contains(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() - 1]))
								{
									selected = grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() - 1];
									break;
								}
								else
									frontier.add(grid[frontier.get(0).getCoordX()][frontier.get(0).getCoordY() - 1]);
							frontier.remove(frontier.get(0));
						}
						ArrayList<Grid> currentPath = new ArrayList<>();
						while (selected != grid[coord[player][0]][coord[player][1]])
						{
							currentPath.add(selected);
							selected = grid[selected.getParentX()][selected.getParentY()];
						}
						tempPath.add(currentPath);
						break;
					}
				
				//Select an open cell based on h-value
				for (int i = 0, min = 60; i < open.size(); i++)
					if (open.get(i).getH() < min)
					{
						min = open.get(i).getH();
						selected = open.get(i);
					}
				open.remove(open.indexOf(selected));
				closed.add(selected);
				
				//Opens adjacent cells
				if (selected.getCoordX() + 1 < 13 && grid[selected.getCoordX() + 1][selected.getCoordY()] != null && grid[selected.getCoordX() + 1][selected.getCoordY()].getText().equals(" ") && !open.contains(grid[selected.getCoordX() + 1][selected.getCoordY()]) && !closed.contains(grid[selected.getCoordX() + 1][selected.getCoordY()]))
				{
					open.add(grid[selected.getCoordX() + 1][selected.getCoordY()]);
					grid[selected.getCoordX() + 1][selected.getCoordY()].setParentX(selected.getCoordX());
					grid[selected.getCoordX() + 1][selected.getCoordY()].setParentY(selected.getCoordY());
					if (grid[selected.getCoordX() + 1][selected.getCoordY()] == destination)
					{
						selected = grid[selected.getCoordX() + 1][selected.getCoordY()];
						destinationFound = true;
					}
				}
				if (selected.getCoordX() - 1 >= 0 && grid[selected.getCoordX() - 1][selected.getCoordY()] != null && grid[selected.getCoordX() - 1][selected.getCoordY()].getText().equals(" ") && !open.contains(grid[selected.getCoordX() - 1][selected.getCoordY()]) && !closed.contains(grid[selected.getCoordX() - 1][selected.getCoordY()]))
				{
					open.add(grid[selected.getCoordX() - 1][selected.getCoordY()]);
					grid[selected.getCoordX() - 1][selected.getCoordY()].setParentX(selected.getCoordX());
					grid[selected.getCoordX() - 1][selected.getCoordY()].setParentY(selected.getCoordY());
					if (grid[selected.getCoordX() - 1][selected.getCoordY()] == destination)
					{
						selected = grid[selected.getCoordX() - 1][selected.getCoordY()];
						destinationFound = true;
					}
				}
				if (selected.getCoordY() + 1 < 13 && grid[selected.getCoordX()][selected.getCoordY() + 1] != null && grid[selected.getCoordX()][selected.getCoordY() + 1].getText().equals(" ")  && !open.contains(grid[selected.getCoordX()][selected.getCoordY() + 1]) && !closed.contains(grid[selected.getCoordX()][selected.getCoordY() + 1]))
				{
					open.add(grid[selected.getCoordX()][selected.getCoordY() + 1]);
					grid[selected.getCoordX()][selected.getCoordY() + 1].setParentX(selected.getCoordX());
					grid[selected.getCoordX()][selected.getCoordY() + 1].setParentY(selected.getCoordY());
					if (grid[selected.getCoordX()][selected.getCoordY() + 1] == destination)
					{
						selected = grid[selected.getCoordX()][selected.getCoordY() + 1];
						destinationFound = true;
					}
				}
				if (selected.getCoordY() - 1 >= 0 && grid[selected.getCoordX()][selected.getCoordY() - 1] != null && grid[selected.getCoordX()][selected.getCoordY() - 1].getText().equals(" ")  && !open.contains(grid[selected.getCoordX()][selected.getCoordY() - 1]) && !closed.contains(grid[selected.getCoordX()][selected.getCoordY() - 1]))
				{
					open.add(grid[selected.getCoordX()][selected.getCoordY() - 1]);
					grid[selected.getCoordX()][selected.getCoordY() - 1].setParentX(selected.getCoordX());
					grid[selected.getCoordX()][selected.getCoordY() - 1].setParentY(selected.getCoordY());
					if (grid[selected.getCoordX()][selected.getCoordY() - 1] == destination)
					{
						selected = grid[selected.getCoordX()][selected.getCoordY() - 1];
						destinationFound = true;
					}
				}
				
				if (destinationFound)
				{
					ArrayList<Grid> currentPath = new ArrayList<>();
					while (selected != grid[coord[player][0]][coord[player][1]])
					{
						currentPath.add(selected);
						selected = grid[selected.getParentX()][selected.getParentY()];
					}
					tempPath.add(currentPath);
					break;
				}
			}
		} // End of start loop
		int index = 0;
		if (destinationFound)
		{
			for (int i = 1, small = tempPath.get(0).size(); i < tempPath.size(); i++)
			{
				if (tempPath.get(i).size() < small)
				{
					small = tempPath.get(i).size();
					index = i;
				}
			}
			
			for (int i = 0; i < tempPath.get(index).size(); i++)
			{
				System.out.println("X: " + tempPath.get(index).get(i).getCoordX() + "\tY: " + tempPath.get(index).get(i).getCoordY());
			}
			System.out.println();
			
			path.add(tempPath.get(index));
		}
	}
	
	//Moves AI
	private static void moveAI(int player, ArrayList<Grid> path) //TODO minor bug: incorrect room chosen sometimes
	{
		//Broken, does not work completely
		JPanel turnP = new JPanel();
		turnP.setLayout(null);
		mainFrame.setContentPane(turnP);
		updateGrid(turnP);
		
		JLabel moveL = new JLabel("MOVES: " + move);
		moveL.setBounds(53, 475, 150, 20);
		turnP.add(moveL);
		
		JLabel playerL = new JLabel("PLAYER: " + (player + 1) + " (" + PERSON[player] + ")");
		playerL.setBounds(21, 5, 200, 20);
		turnP.add(playerL);
		
		//Use Timer
		ActionListener loopAction = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
		    	if (path.isEmpty() || move == 0)
		    	{
		    		if (grid[coord[player][0]][coord[player][1]].checkDoor() && move >= 1)
		    		{
		    			turnP.removeAll();
		    			turnP.add(moveL);
		    			turnP.add(playerL);
		    			moveRoom(player, grid[coord[player][0]][coord[player][1]].getRoom(), coord[player][0], coord[player][1]);
		    			moveL.setText("MOVES: " + move);
		    			updateGrid(turnP);
		    			for (ActionListener al : ((Timer)e.getSource()).getActionListeners())
		    				((Timer)e.getSource()).removeActionListener(al);
		    			((Timer)e.getSource()).addActionListener(new ActionListener()
		    			{
		    				@Override
		    				public void actionPerformed(ActionEvent e1)
		    				{
		    					((Timer)e1.getSource()).stop();
		    					guessAI(player, ai[player - players].getRoom());
		    				}
		    			});
		    			((Timer)e.getSource()).restart();
		    		}
		    		else
		    		{
		    			((Timer)e.getSource()).stop();
		    			switchPlayer(player);
		    		}
		    			
		    	}
		    	else
		    	{
					updateGrid(turnP);
					grid[coord[player][0]][coord[player][1]].setText(" ");
					coord[player][0] = path.get(path.size() - 1).getCoordX();
					coord[player][1] = path.get(path.size() - 1).getCoordY();
					path.get(path.size() - 1).setText(Integer.toString(player + 1));
					path.get(path.size() - 1).setBackground(Color.red);
					path.remove(path.get(path.size() - 1));
					move--;
					moveL.setText("MOVES: " + move);
					
					turnP.revalidate();
			    	turnP.repaint();
		    	}
			}
		};
		Timer loopTimer = new Timer(1000, loopAction);
		loopTimer.setInitialDelay(1000);
		
		ActionListener action = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				grid[coord[player][0]][coord[player][1]].setBackground(Color.red);
				turnP.revalidate();
		    	turnP.repaint();
		    	((Timer)e.getSource()).stop();
		    	loopTimer.start();
			}
		};
		Timer timer = new Timer(1, action);
		timer.setInitialDelay(1000);
		timer.start();
		
	}
	
	//Moves player into a room
	private static void moveRoom(int player, int roomNum, int x, int y)
	{
		move--;
		grid[x][y].setText(" ");
		coord[player][0] = 1;
		coord[player][1] = 1;
		roomB[roomNum].setName(roomB[roomNum].getName() + player);
	}
	
	//Moves player to another tile
	private static void move(int player, int x, int y)
	{
		move--;
		if (grid[coord[player][0]][coord[player][1]] != null)
			grid[coord[player][0]][coord[player][1]].setText(" ");
		grid[x][y].setText(Integer.toString(player+1));
		coord[player][0] = x;
		coord[player][1] = y;
	}
	
	private static void guessAI(int player, int roomNum)
	{
		
	}
	
	//Player to make a definitive accusation
	private static void accuse(int player)
	{
		JPanel confirmJP = new JPanel();
		confirmJP.setLayout(null);
		mainFrame.setContentPane(confirmJP);
		JPanel accuse1JP = new JPanel();
		accuse1JP.setLayout(null);
		JPanel accuse2JP = new JPanel();
		accuse2JP.setLayout(null);
		JPanel accuse3JP = new JPanel();
		accuse3JP.setLayout(null);
		JPanel resultJP = new JPanel();
		resultJP.setLayout(null);
		
		JLabel confirmL = new JLabel("If you make a false accusation, you will lose the game. Do you wish to continue?");
		confirmL.setBounds(150, 200, 400, 50);
		confirmJP.add(confirmL);
		
		JButton yesB = new JButton("YES");
		yesB.setBounds(150, 500, 100, 30);
		yesB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mainFrame.setContentPane(accuse1JP);
				accuse1JP.repaint();
				accuse1JP.revalidate();
			}
		});
		confirmJP.add(yesB);
		
		JButton noB = new JButton("NO");
		noB.setBounds(400, 500, 100, 30);
		noB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				turnPlayer(player);
			}
		});
		confirmJP.add(noB);
		
		confirmJP.repaint();
		confirmJP.revalidate();
		
		JLabel playerL = new JLabel();
		playerL.setBounds(200, 75, 100, 30);
		accuse3JP.add(playerL);
		JLabel infoLabel = new JLabel();
		infoLabel.setBounds(10, 10, 200, 100);
		accuse3JP.add(infoLabel);
		
		JLabel accuseLabel = new JLabel("Who did it?");
		accuseLabel.setBounds(200, 100, 250, 30);
		accuse1JP.add(accuseLabel);
		
		//Set up JPanel 1 for player selection
		JButton[] personB = new JButton[6];
		for (int i = 0; i < personB.length; i++)
		{
			personB[i] = new JButton(PERSON[i]);
			personB[i].setName(Integer.toString(i));
			personB[i].setBounds(i * 110 + 15, 500, 100, 30);
			personB[i].setBackground(infoP[player][i].getColor());
			personB[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					guessP = Integer.parseInt(button.getName());
					
					accuseLabel.setText("What is the murder weapon?");
					accuse2JP.add(accuseLabel);
					
					mainFrame.setContentPane(accuse2JP);
					accuse2JP.repaint();
					accuse2JP.revalidate();
				}
			});
			accuse1JP.add(personB[i]);
		}
		
		//Set up JPanel 2 for weapon selection
		JButton[] weaponB = new JButton[5];
		for (int i = 0; i < weaponB.length; i++)
		{
			weaponB[i] = new JButton(WEAPON[i]);
			weaponB[i].setName(Integer.toString(i));
			weaponB[i].setBounds(i * 110 + 15, 500, 100, 30);
			weaponB[i].setBackground(infoW[player][i].getColor());
			weaponB[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					guessW = Integer.parseInt(button.getName());
					
					accuseLabel.setText("Where did it happen?");
					accuse3JP.add(accuseLabel);
					
					mainFrame.setContentPane(accuse3JP);
					accuse3JP.repaint();
					accuse3JP.revalidate();
				}
			});
			accuse2JP.add(weaponB[i]);
		}
		
		//Set up JPanel 3 for room selection
		JButton[] locationB = new JButton[9];
		for (int i = 0; i < locationB.length; i++)
		{
			locationB[i] = new JButton(LOCATION[i]);
			locationB[i].setName(Integer.toString(i));
			if (i < 5)
				locationB[i].setBounds(i * 110 + 15, 450, 100, 30);
			else
				locationB[i].setBounds((i - 5) * 110 + 15, 500, 100, 30);
			locationB[i].setBackground(infoL[player][i].getColor());
			locationB[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					guessL = Integer.parseInt(button.getName());
					
					if (guessP == accuseP && guessW == accuseW && guessL == accuseL)
						accuseLabel.setText("Correct! You win!");
					else
						accuseLabel.setText("INCORRECT! You can no longer continue.");
					resultJP.add(accuseLabel);
					
					mainFrame.setContentPane(resultJP);
					resultJP.repaint();
					resultJP.revalidate();
				}
			});
			accuse3JP.add(locationB[i]);
		}
		
		//Set up resultJP
		JButton resultB = new JButton("CONTINUE");
		resultB.setBounds(400, 500, 100, 30);
		resultB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (guessP == accuseP && guessW == accuseW && guessL == accuseL)
					start("players"); //This may need to change
				else
				{
					if (grid[coord[player][0]][coord[player][1]] != null)
						grid[coord[player][0]][coord[player][1]].setText(" ");

					loosers[player] = true;
					switchPlayer(player);
				}
			}
		});
		resultJP.add(resultB);
	}
	
	//Player makes guess on what happened
	private static void guess(int player, int roomNum)
	{
		guessed = true;
		guessL = roomNum;
		
		JPanel guess1JP = new JPanel();
		guess1JP.setLayout(null);
		mainFrame.setContentPane(guess1JP);
		JPanel guess2JP = new JPanel();
		guess2JP.setLayout(null);
		JPanel playerJP = new JPanel();
		playerJP.setLayout(null);
		JPanel selectJP = new JPanel();
		selectJP.setLayout(null);
		
		JLabel playerL = new JLabel();
		playerL.setBounds(200, 75, 100, 30);
		playerJP.add(playerL);
		JLabel infoLabel = new JLabel();
		infoLabel.setBounds(10, 10, 200, 100);
		playerJP.add(infoLabel);
		
		JLabel personL = new JLabel("Who do you think did it?");
		personL.setBounds(300, 100, 300, 100);
		guess1JP.add(personL);
		
		//Set up JPanel 1 for player selection
		JButton[] personB = new JButton[6];
		for (int i = 0; i < personB.length; i++)
		{
			personB[i] = new JButton(PERSON[i]);
			personB[i].setName(Integer.toString(i));
			personB[i].setBounds(i * 110 + 15, 500, 100, 30);
			personB[i].setBackground(infoP[player][i].getColor());
			personB[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					guessP = Integer.parseInt(button.getName());
					
					mainFrame.setContentPane(guess2JP);
					guess2JP.repaint();
					guess2JP.revalidate();
				}
			});
			guess1JP.add(personB[i]);
		}
		
		//Set up JPanel 2 for weapon selection
		JButton[] weaponB = new JButton[5];
		for (int i = 0; i < weaponB.length; i++)
		{
			weaponB[i] = new JButton(WEAPON[i]);
			weaponB[i].setName(Integer.toString(i));
			weaponB[i].setBounds(i * 110 + 15, 500, 100, 30);
			weaponB[i].setBackground(infoW[player][i].getColor());
			weaponB[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton button = (JButton)e.getSource();
					guessW = Integer.parseInt(button.getName());
					mainFrame.setContentPane(playerJP);
					infoLabel.setText("<html>Location: " + roomB[guessL].getName() + "<br>Person: " + (guessP + 1) + "<br>Weapon: " + (guessW + 1));
				}
			});
			guess2JP.add(weaponB[i]);
		}
		
		//Set up Player JPanel for players to prove this player wrong
		JButton[] playerNext = new JButton[playerCount - 1];
		for (int i = 0, p = player + 2; i < playerNext.length; i++, p++)
		{
			if (p > playerCount) p = 1;
			playerNext[i] = new JButton("CONTINUE");
			playerNext[i].setName(Integer.toString(p) + i);
		}
		for (int i = 0; i < playerNext.length; i++)
		{
			playerNext[i].setBounds(400, 500, 100, 30);
			playerNext[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					int buttonName = Integer.parseInt(((JButton)e.getSource()).getName().substring(0, 1));
					int buttonIndex = Integer.parseInt(((JButton)e.getSource()).getName().substring(1));
					
					System.out.println("INDEX " + buttonIndex);
					System.out.println("NAME " + buttonName);
					
					if (buttonIndex + 1 < playerNext.length)
					{
						playerJP.add(playerNext[buttonIndex + 1]);
						playerL.setText("Player " + playerNext[buttonIndex + 1].getName().toString().substring(0, 1) + " card check");
					}
					playerJP.remove(playerNext[buttonIndex]);
					playerJP.repaint();
					playerJP.revalidate();
					
					if (guessP < playerCount)
					{
						coord[guessP][0] = coord[player][0];
						coord[guessP][1] = coord[player][1];
					}
					if (infoW[buttonName - 1][guessW] == Info.CARD)
					{
						System.out.println("Player to prove: " + buttonName);
						proof(player, buttonName - 1);
					}
					else if (infoP[buttonName - 1][guessP] == Info.CARD)
					{
						System.out.println("Player to prove: " + buttonName);
						proof(player, buttonName - 1);
					}
					else if (infoL[buttonName - 1][guessL] == Info.CARD)
					{
						System.out.println("Player to prove: " + buttonName);
						proof(player, buttonName - 1);
					}
					else if (buttonIndex == playerNext.length - 1)
						turnPlayer(player);
				}
			});
			if (player + 1 < playerCount)
				playerL.setText("Player " + playerNext[0].getName().substring(0, 1) + " card check");
			else
				playerL.setText("Player " + 1 + " card check");
		}
		playerJP.add(playerNext[0]);
		
		guess1JP.repaint();
		guess1JP.revalidate();
	}
	
	private static void proof(int player, int prover)
	{	
		JPanel proofJP = new JPanel();
		proofJP.setLayout(null);
		mainFrame.setContentPane(proofJP);
		JPanel playerJP = new JPanel();
		playerJP.setLayout(null);
		
		JButton buttonL = new JButton(LOCATION[guessL]);
		JButton buttonW = new JButton(WEAPON[guessW]);
		JButton buttonP = new JButton(PERSON[guessP]);
		
		JLabel proverL = new JLabel("Which card will you show, player " + (player + 1) + "?");
		proverL.setBounds(200, 75, 200, 50);
		proofJP.add(proverL);
		
		JLabel playerL = new JLabel("Player " + (prover + 1) + " has showed you this card:");
		playerL.setBounds(200, 75, 200, 50);
		
		if (infoW[prover][guessW] == Info.CARD)
		{
			buttonW.setBounds(62, 275, 150, 50);
			buttonW.setName("A");
			buttonW.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (((JButton)e.getSource()).getName().equals("A"))
					{
						proofJP.remove(buttonL);
						proofJP.remove(buttonP);
						proofJP.remove(proverL);
						proofJP.add(playerL);
						((JButton)e.getSource()).setName("B");
						
						proofJP.repaint();
						proofJP.revalidate();
					}
					else
					{
						infoW[player][guessW] = Info.KNOWLEDGE;
						turnPlayer(player);
					}
				}
			});
			proofJP.add(buttonW);
		}
		if (infoP[prover][guessP] == Info.CARD)
		{
			buttonP.setBounds(274, 275, 150, 50);
			buttonP.setName("A");
			buttonP.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (((JButton)e.getSource()).getName().equals("A"))
					{
						proofJP.remove(buttonL);
						proofJP.remove(buttonW);
						proofJP.remove(proverL);
						proofJP.add(playerL);
						((JButton)e.getSource()).setName("B");
						
						proofJP.repaint();
						proofJP.revalidate();
					}
					else
					{
						infoP[player][guessP] = Info.KNOWLEDGE;
						turnPlayer(player);
					}
				}
			});
			proofJP.add(buttonP);
		}
		if (infoL[prover][guessL] == Info.CARD)
		{
			buttonL.setBounds(486, 275, 150, 50);
			buttonL.setName("A");
			buttonL.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if (((JButton)e.getSource()).getName().equals("A"))
					{
						proofJP.remove(buttonW);
						proofJP.remove(buttonP);
						proofJP.remove(proverL);
						proofJP.add(playerL);
						((JButton)e.getSource()).setName("B");
						
						proofJP.repaint();
						proofJP.revalidate();
					}
					else
					{
						infoL[player][guessL] = Info.KNOWLEDGE;
						turnPlayer(player);
					}
				}
			});
			proofJP.add(buttonL);
		}
		
		proofJP.repaint();
		proofJP.revalidate();
	}
	
	//Displays grid system to JPanel
	private static void updateGrid(JPanel jp)
	{
		//Displays grid spaces
		for (int x = 0; x < 13; x++)
			for (int y = 0; y < 13; y++)
				if (grid[x][y] != null)
				{
					grid[x][y].setBackground(new JButton().getBackground()); //Sets JButton color back to default
					for(ActionListener al : grid[x][y].getActionListeners()) grid[x][y].removeActionListener(al); //Removes all actionListener from the current JButton
					jp.add(grid[x][y]);
				}
		//Display Rooms
		for (int i = 0; i < roomB.length; i++)
		{
			roomB[i].setBackground(new JButton().getBackground());
			jp.add(roomB[i]);
		}
		
		String playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[0].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[0].setText("<html>" + LOCATION[0] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[1].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[1].setText("<html>" + LOCATION[1] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[2].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[2].setText("<html>" + LOCATION[2] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[3].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[3].setText("<html>" + LOCATION[3] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[4].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[4].setText("<html>" + LOCATION[4] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[5].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[5].setText("<html>" + LOCATION[5] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[6].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[6].setText("<html>" + LOCATION[6] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[7].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[7].setText("<html>" + LOCATION[7] + "<br>" + playerNum + "</html>");
		
		playerNum = "";
		for (int i = 0; i < playerCount; i++)
			if (roomB[8].getName().contains(Integer.toString(i)) && !loosers[i])
				playerNum += Integer.toString(i + 1) + " ";
		roomB[8].setText("<html>" + LOCATION[8] + "<br>" + playerNum + "</html>");
		
		jp.revalidate();
		jp.repaint();
	}
}
