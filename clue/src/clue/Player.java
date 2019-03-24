package clue;

public class Player {
  
  private Info[] wInfo;
  private Info[] pInfo;
  private Info[] lInfo;
  private Info room;
  
  public Player() {
    
   } 
  
  public Info getRoom() {
    return room;
   } 
  
  public boolean isInRoom() {
    if (room != null) 
      return false;
    return true;
   } 
  
 } 
