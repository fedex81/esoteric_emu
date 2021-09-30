package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
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
public class selectBrowser extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private JTextField BrowserSelection;
	private JButton CancelButton;
	private JButton OkButton;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				selectBrowser inst = new selectBrowser();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public selectBrowser() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

			this.setTitle("Please Enter the name of your Browser");
			FlowLayout thisLayout = new FlowLayout();
			getContentPane().setLayout(thisLayout);
			this.setResizable(false);
			JFrame.setDefaultLookAndFeelDecorated(true);
			{
				BrowserSelection = new JTextField();
				getContentPane().add(BrowserSelection);
				BrowserSelection.setText("jTextField1");
				BrowserSelection.setPreferredSize(new java.awt.Dimension(316, 26));
			}
			{
				OkButton = new JButton();
				getContentPane().add(OkButton);
				OkButton.setLayout(null);
				OkButton.setText("OK");
				OkButton.setPreferredSize(new java.awt.Dimension(64, 32));
			}
			{
				CancelButton = new JButton();
				getContentPane().add(CancelButton);
				CancelButton.setText("Cancel");
				CancelButton.setPreferredSize(new java.awt.Dimension(77, 32));
			}
			pack();
			this.setSize(388, 98);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
