package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
public class MyImagePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BufferedImage img; 
	
	public MyImagePanel() {
		super();
	}

   MyImagePanel (BufferedImage img) 
   { 
	   super();
	   this.img = img; 
   } 
 
   public int getWidth(){
	   return img.getWidth();
   }
   
   public int getHeight(){
	   return img.getHeight();
   }
   public void paint (Graphics g) {    
	   g.drawImage(img, 0, 0,null); 
   }

}
