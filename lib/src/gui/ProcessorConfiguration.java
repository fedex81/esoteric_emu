package gui;
import java.awt.BorderLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

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
public class ProcessorConfiguration extends javax.swing.JFrame {

    {
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ProcessorConfiguration inst = new ProcessorConfiguration();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	private ProcessorConfiguration() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			this.setResizable(false);
			this.setTitle("Select Cpu Emulation Method");
			getContentPane().setLayout(null);
			{
                JLabel objDescription = new JLabel();
				getContentPane().add(objDescription);
				objDescription.setText("Cpu Emulation Method");
				objDescription.setBounds(17, 20, 183, 15);
				objDescription.setFont(new java.awt.Font("Andale Mono",1,12));
			}
			{
				ComboBoxModel cpuListModel = 
					new DefaultComboBoxModel(
							new String[] { "Item One", "Item Two" });
                JComboBox cpuList = new JComboBox();
				getContentPane().add(cpuList);
				cpuList.setModel(cpuListModel);
				cpuList.setBounds(17, 60, 239, 24);
			}
			{
                JButton okButton = new JButton();
				getContentPane().add(okButton);
				okButton.setText("Apply");
				okButton.setBounds(103, 114, 71, 25);
			}
			{
                JButton cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("Cancel");
				cancelButton.setBounds(179, 114, 74, 25);
			}
			pack();
			this.setSize(281, 180);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
