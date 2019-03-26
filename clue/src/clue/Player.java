package clue;

public class Player {
  
  private Info[] wInfo;
  private Info[] pInfo;
  private Info[] lInfo;
  private int[] location;
  private boolean inRoom;
  private String imagePath;
  
  public Player(int w, String image) {
    wInfo = new Info[w];
    for (int i = 0; i < wInfo.length; i++) {
      //wInfo[i] = Info.???
     //kill me now
     } 
    pInfo = new Info[6];
    pInfo = new Info[9];
    
    location = new int[2];
    
    inRoom = false;
    
    imagePath = image;
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
  
  public boolean checkInfoW(int index) {
    if (wInfo[index].isCard())
      return true;
    return false;
   } 
  
  public boolean checkInfoP(int index) {
    if (pInfo[index].isCard())
      return true;
    return false;
   } 
  
  public boolean checkInfoL(int index) {
    if (lInfo[index].isCard())
      return true;
    return false;
   } 
  
  public int[] getLocation() {
    return location;
   } 
  
  public boolean isInRoom() {
    return inRoom;
   } 
  
 } 
