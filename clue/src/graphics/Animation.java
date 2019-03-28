package graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;



/**
 * Totally not stealing this from a game I helped make
 * Created by sn317602 on 11/18/2016.
 */
public class Animation {
    private BufferedImage spriteSheet;
    private BufferedImage frame;
    private int scaling;
    private int currentFrame;
    private int frameHeight;
    private int frameWidth;
    private int framesPerRow;
    public int numberOfFrames;
    public int time;

    //Initializing Animation
    public Animation(BufferedImage s, int w, int h, int num, int column) {
        spriteSheet = s;
        frameHeight = h;
        frameWidth = w;
        numberOfFrames = num;
        framesPerRow = column;
        setFrame(0);
        scaling = 100;
    }

    //Temporary Background Creator - MAKE SURE TO MAKE BACKGROUND CLASS
    public Animation(int t,BufferedImage s, int w, int h, int num, int column) {
        spriteSheet = s;
        time = t;
        frameHeight = h;
        frameWidth = w;
        numberOfFrames = num;
        framesPerRow = column;
        setFrame(0);
    }

    //set image Scale
    public void setScale(int scale) {
    	this.scaling = scale;
    }
    
    
    //Changing Animation Frames
    public void setFrame(int frameNumber) {
    	if(spriteSheet != null) {
	        if(frameNumber < numberOfFrames) {
	            currentFrame = frameNumber;
	            int row = (frameNumber) % framesPerRow;
	            int column = (frameNumber) / framesPerRow;
	            frame = spriteSheet.getSubimage(row * frameWidth, column * frameHeight, frameWidth, frameHeight);
	        } else {
	            //System.out.println("Error: Can't change Frames");
	        }
    	}
    }

    public void nextFrame(){
        if(currentFrame + 1 < numberOfFrames) {
            setFrame(currentFrame + 1);
        }else{
            setFrame(0);
        }
    }

    //Returning Frame
    public BufferedImage getFrame() {
    	return frame;
        /*return (BufferedImage) frame.getScaledInstance((int)(frameWidth * ((double) scaling / 100)), 
        		(int)(frameHeight * ((double) scaling / 100)), 
        		Image.SCALE_DEFAULT);*/
    }
    
    public int getFrameIndex() {
    	return currentFrame;
    }
}

