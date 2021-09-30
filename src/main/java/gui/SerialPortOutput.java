package gui;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

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
public class SerialPortOutput extends javax.swing.JFrame {
	private JPanel BackgroundPane;
	private JEditorPane jEditorPane1;

	
	public SerialPortOutput() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			this.setTitle("Serial Port Output");
			{
				BackgroundPane = new JPanel();
				getContentPane().add(BackgroundPane, BorderLayout.CENTER);
				BackgroundPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				{
					jEditorPane1 = new JEditorPane();
					BackgroundPane.add(jEditorPane1);
					jEditorPane1.setPreferredSize(new java.awt.Dimension(555, 247));
					jEditorPane1.setBackground(new java.awt.Color(0,0,0));
				}
			}
			pack();
			this.setSize(581, 293);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setText(char c){
	}

}
