package clue;

public class Player {
  
  private Info[] wInfo;
  private Info[] pInfo;
  private Info[] lInfo;
  private Info room;
  
  public Player(int w) {
    wInfo = new Info[w];
    for (int i = 0; i < wInfo.length; i++) {
      //wInfo[i] = Info.???
     //kill me now
     } 
    pInfo = new Info[6];
    pInfo = new Info[9];
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
