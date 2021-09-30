package gui;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
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
public class PathInput extends javax.swing.JFrame {
	private JLabel Bios;
	private JTextField biosPath;
	private JButton browseFlash;
	private JButton browseBios;
	private JButton cancelButton;
	private JButton okButton;
	private JTextField flashTextInput;
	private JLabel flashLabel;
	
	public PathInput() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Please enter the paths to the bios files");
			this.setFont(new java.awt.Font("Andale Mono",1,10));
			{
				Bios = new JLabel();
				getContentPane().add(Bios);
				Bios.setText("Bios");
				Bios.setBounds(21, 27, 46, 15);
				Bios.setFont(new java.awt.Font("Andale Mono",1,12));
			}
			{
				biosPath = new JTextField();
				getContentPane().add(biosPath);
				biosPath.setBounds(67, 24, 217, 22);
			}
			{
				flashLabel = new JLabel();
				getContentPane().add(flashLabel);
				flashLabel.setText("Flash");
				flashLabel.setBounds(21, 70, 35, 15);
			}
			{
				flashTextInput = new JTextField();
				getContentPane().add(flashTextInput);
				flashTextInput.setBounds(68, 67, 216, 22);
			}
			{
				okButton = new JButton();
				getContentPane().add(okButton);
				okButton.setText("OK");
				okButton.setBounds(231, 115, 73, 22);
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("Cancel");
				cancelButton.setBounds(315, 115, 76, 22);
			}
			{
				browseBios = new JButton();
				getContentPane().add(browseBios);
				browseBios.setText("BROWSE");
				browseBios.setBounds(310, 24, 94, 22);
			}
			{
				browseFlash = new JButton();
				getContentPane().add(browseFlash);
				browseFlash.setText("BROWSE");
				browseFlash.setBounds(310, 67, 94, 21);
			}
			pack();
			this.setSize(416, 177);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
