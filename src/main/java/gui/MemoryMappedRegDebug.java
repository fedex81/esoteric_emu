package gui;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
public class MemoryMappedRegDebug extends javax.swing.JFrame {
	private JLabel Description;
	private JButton jButton1;
	private JTable RegisterTable;

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
				MemoryMappedRegDebug inst = new MemoryMappedRegDebug();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public MemoryMappedRegDebug() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Memory Mapped Registers Viewer");
			this.setResizable(false);
			{
				Description = new JLabel();
				getContentPane().add(Description);
				Description.setText("Memory Mapped Sh4 Registers");
				Description.setBounds(21, 7, 230, 15);
				Description.setFont(new java.awt.Font("Andale Mono",1,12));
			}
			{
				TableModel RegisterTableModel = 
					new DefaultTableModel(
							new String[][] { { "One", "Two" }, { "Three", "Four" } },
							new String[] { "Column 1", "Column 2" });
				RegisterTable = new JTable(19,4);
				getContentPane().add(RegisterTable);
				RegisterTable.setModel(RegisterTableModel);
				RegisterTable.setBounds(26, 47, 497, 300);
				RegisterTable.setAutoCreateColumnsFromModel(false);
				RegisterTable.setAutoscrolls(false);
				RegisterTable.setRowSelectionAllowed(false);
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setText("Close");
				jButton1.setBounds(441, 365, 82, 25);
			}
			pack();
			this.setSize(557, 431);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
