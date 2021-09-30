package gui;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
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
class SerialPortOutput extends javax.swing.JFrame {
	

    private static JTextArea Text;
	private static final StringBuffer Buffer = new StringBuffer(128);
	private static int lenght=0;
	
	public SerialPortOutput() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			setTitle("Serial Port Output");
			{
                JScrollPane jPanel1 = new JScrollPane();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				jPanel1.setPreferredSize(new java.awt.Dimension(417, 330));
				{
					Text = new JTextArea();
					Text.setText("jTextPane1");
					Text.setPreferredSize(new java.awt.Dimension(481, 362));
					Text.setBackground(new java.awt.Color(0,0,0));
					Text.setIgnoreRepaint(true);
					Text.setFont(new java.awt.Font("Andale Mono",0,12));
					Text.setForeground(new java.awt.Color(255,255,255));
				}
				jPanel1.setViewportView(Text);
			}
			pack();
			this.setSize(500, 400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void addChar(char c){
		Buffer.insert(lenght++, c);
	}
	
	public static void appendBuffer(){
		System.out.println("GETS HERE");
		Text.append(Buffer.toString());
		Text.repaint();
		lenght =0;
	}

}
