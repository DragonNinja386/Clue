package clue;

public class Player {
  
  private Info[] wInfo;
  private Info[] pInfo;
  private Info[] lInfo;
  private int[] location;
  private boolean inRoom;
  
  public Player(int w) {
    wInfo = new Info[w];
    for (int i = 0; i < wInfo.length; i++) {
      //wInfo[i] = Info.???
     //kill me now
     } 
    pInfo = new Info[6];
    pInfo = new Info[9];
    
    location = new int[2];
    
    inRoom = false;
   } 
  
  public void toggleRoom() {
    if (inRoom) 
      inRoom = false;
    else
      inRoom = true;
   } 
  
  public void setLocation(int[] l) {
    location = l;
   } 
  
  public int[] getLocation() {
    return location;
   } 
  
  public boolean isInRoom() {
    return inRoom;
   } 
  
 } 
