/*
 * jCDChooser.java
 *
 * Created on 22 de Junho de 2008, 2:22
 */
package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import io.libJia.CdioLib;

/**
 *
 * @author  Kofi
 */
public class jCDChooser extends javax.swing.JFrame {


	private JLabel jLabel1;
	private JButton Refresh;
	private JButton CancelButton;
	private JButton OKButton;
	private JComboBox DriveSelectionBox;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jCDChooser inst = new jCDChooser();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public jCDChooser(){
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			getContentPane().setLayout(thisLayout);
			this.setResizable(false);
			JFrame.setDefaultLookAndFeelDecorated(true);
			{
				DriveSelectionBox = new JComboBox();
				getContentPane().add(DriveSelectionBox, BorderLayout.CENTER);
				DriveSelectionBox.setPreferredSize(new java.awt.Dimension(385, 78));
				DriveSelectionBox.setVerifyInputWhenFocusTarget(false);
				DriveSelectionBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						DriveSelectionBoxActionPerformed(evt);
					}
				});
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, BorderLayout.NORTH);
				jLabel1.setText("Available Drives");
				jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
			}
			{
				Refresh = new JButton();
				getContentPane().add(Refresh, BorderLayout.WEST);
				Refresh.setText("Refresh");
				Refresh.setPreferredSize(new java.awt.Dimension(96, 31));
				Refresh.setHorizontalTextPosition(SwingConstants.LEFT);
				Refresh.setHorizontalAlignment(SwingConstants.LEFT);
				Refresh.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						RefreshActionPerformed(evt);
					}
				});
			}
			{
				CancelButton = new JButton();
				getContentPane().add(CancelButton, BorderLayout.SOUTH);
				CancelButton.setText("Cancel");
				CancelButton.setVerticalAlignment(SwingConstants.TOP);
				CancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						CancelButtonActionPerformed(evt);
					}
				});
			}
			{
				OKButton = new JButton();
				getContentPane().add(OKButton, BorderLayout.EAST);
				OKButton.setText("OK");
				OKButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						OKButtonActionPerformed(evt);
					}
				});
			}
			pack();
			this.setSize(447, 99);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void RefreshActionPerformed(ActionEvent evt) {
		CdioLib.cdInterface.getCDRomDevices();
    	int drives = CdioLib.cdInterface.getNumberOfAvailableDevices();
		jLabel1.setText("Available Drives  ---> " + drives);
		for(int i =0; i < drives; i++)
			DriveSelectionBox.addItem(CdioLib.devices.getString(i*128));
	}
	
	private void OKButtonActionPerformed(ActionEvent evt) {
	}
	
	private void DriveSelectionBoxActionPerformed(ActionEvent evt) {
	}
	
	private void CancelButtonActionPerformed(ActionEvent evt) {
		this.setVisible(false);
	}

    
}
