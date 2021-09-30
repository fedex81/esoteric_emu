package gui;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.WindowConstants;
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
public class BrowserView extends javax.swing.JFrame {


    /**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BrowserView inst = new BrowserView();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	private BrowserView() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setResizable(false);
			{
                JPanel infoPanel = new JPanel();
				getContentPane().add(infoPanel);
				infoPanel.setBounds(5, 12, 367, 129);
				infoPanel.setLayout(null);
				infoPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				{
					ComboBoxModel MethodsSelectionModel = 
						new DefaultComboBoxModel(
								new String[] { "Manual", "Firefox" });
                    JComboBox methodsSelection = new JComboBox();
					infoPanel.add(methodsSelection);
					methodsSelection.setModel(MethodsSelectionModel);
					methodsSelection.setBounds(133, 29, 170, 22);
				}
				{
                    JLabel jLabel1 = new JLabel();
					infoPanel.add(jLabel1);
					jLabel1.setText("Browser:");
					jLabel1.setBounds(14, 33, 101, 15);
					jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
                    JLabel jLabel2 = new JLabel();
					infoPanel.add(jLabel2);
					jLabel2.setText("Manual:");
					jLabel2.setBounds(2, 83, 119, 15);
					jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
                    JTextField manualSpecifiedBrowser = new JTextField();
					infoPanel.add(manualSpecifiedBrowser);
					manualSpecifiedBrowser.setText("jTextField1");
					manualSpecifiedBrowser.setBounds(133, 80, 170, 22);
				}
			}
			{
                JPanel jPanel1 = new JPanel();
				FlowLayout jPanel1Layout = new FlowLayout();
				getContentPane().add(jPanel1);
				jPanel1.setBounds(5, 147, 367, 47);
				jPanel1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
				jPanel1.setLayout(jPanel1Layout);
				{
                    JButton jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("OK");
					jButton1.setPreferredSize(new java.awt.Dimension(69, 31));
				}
				{
                    JButton jButton2 = new JButton();
					jPanel1.add(jButton2);
					jButton2.setText("Cancel");
					jButton2.setPreferredSize(new java.awt.Dimension(77, 31));
				}
			}
			pack();
			this.setSize(384, 231);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
