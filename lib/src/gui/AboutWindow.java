package gui;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.SwingUtilities;


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
class AboutWindow extends javax.swing.JFrame {


    public AboutWindow() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setResizable(false);
				this.setTitle("About Esoteric");
				{
                    JPanel jPanel1 = new JPanel();
					getContentPane().add(jPanel1);
					jPanel1.setBounds(0, 0, 443, 375);
					{
                        BufferedImage image = javax.imageio.ImageIO.read(new File("history.jpg"));
                        JPanel imagePanel = new MyImagePanel(image);
						jPanel1.add(imagePanel);
						imagePanel.setPreferredSize(new java.awt.Dimension(image.getWidth(), image.getHeight()));
						imagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					}
                    JScrollPane textPanel;
                    {
						textPanel = new JScrollPane();
						
						textPanel.setPreferredSize(new java.awt.Dimension(424, 107));
						textPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
					jPanel1.add(textPanel);
					{
                        JButton okButton = new JButton();
						jPanel1.add(okButton);
						okButton.setText("Ok");
						okButton.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								setVisible(false);
							}
						});
					}
					{
                        {
                            JTextArea description = new JTextArea();
							textPanel.setViewportView(description);
							description.setText("Esoteric is a proof of concept showing the capabilities of the JVM when used as backend for certain virtualization applications." + System.getProperty("line.separator") + "I would like to thank Ivan Toledo for releasing the sources of dcemu on which this work was base upon,"
									+ " Drk||Raziel for his insights,Nathan Keynes author of Lxdream for making his sources publicly available and to the authors of lwjgl ");
							description.setEditable(false);
							description.setLineWrap(true);
							description.setBackground(Color.LIGHT_GRAY);
							description.setCaretColor(Color.gray);
							description.setBorder(BorderFactory.createLineBorder(Color.black));
							description.setWrapStyleWord(true);
						}
					}
				}
			}
			pack();
			this.setSize(443, 428);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
