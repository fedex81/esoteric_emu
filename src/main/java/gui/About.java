package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.image.BufferedImage;

import gui.MyImagePanel;

import java.io.File;
import java.io.IOException;

import java.awt.Color;
/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class About extends JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JButton OkButton;
	private JTextArea DescriptionField;

	private MyImagePanel ImageContainer;
	
	public About() {
		super();
		initGUI();
	}
	
	private void initGUI() {
//		BufferedImage Image;
		setTitle("About this project");
//		try {
			{
				setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
//				Image  = javax.imageio.ImageIO.read(new File("history.jpg"));
//				ImageContainer = new MyImagePanel(Image);
//				ImageContainer.setPreferredSize(new java.awt.Dimension(Image.getWidth(),Image.getHeight()));
//				getContentPane().add(ImageContainer,BorderLayout.NORTH);
			}
			{
				OkButton = new JButton();
				getContentPane().add(OkButton, BorderLayout.SOUTH);
				OkButton.setText("OK");
			}
			{
				DescriptionField = new JTextArea();
				getContentPane().add(DescriptionField,BorderLayout.CENTER);
				DescriptionField.setText("Esoteric is a proof of concept showing the capabilities of the JVM when used as backend for certain virtualization applications." + System.getProperty("line.separator") + "I would like to thank Ivan Toledo for releasing the sources of dcemu on which this work was base upon");
				DescriptionField.setSize(new java.awt.Dimension(500,10));
				DescriptionField.setEditable(false);
				DescriptionField.setLineWrap(true);
				DescriptionField.setCaretColor(Color.gray);
				DescriptionField.setBorder(BorderFactory.createLineBorder(Color.black));
				DescriptionField.setWrapStyleWord(true);
			}
			setSize(500,10 + 120);
			setResizable(false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
