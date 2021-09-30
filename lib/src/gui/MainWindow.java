package gui;
import io.Loader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceApi;
import org.jvnet.substance.api.SubstanceSkin;
import org.jvnet.substance.skin.SkinInfo;

import sun.applet.Main;

import memory.Memory;


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
public class MainWindow extends javax.swing.JFrame {

	
    private static SerialPortOutput serial;
	static AboutWindow about;
	static JFileChooser filechooser;
	private static JToolBar toolBar;
	static File selected_file; 
	static jCDChooser cd_list;
	private static String selected_cd_drive; 
	private static final Emu emu = new Emu();	
	static Vector<SkinInfo>skin;
	
	static {
		try {
			UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel());
			skin = new Vector<SkinInfo>(org.jvnet.substance.SubstanceLookAndFeel.getAllSkins().values());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	

	
	public MainWindow() {
		super();
		initGUI();
	}
	
	void emuLoop(){
			Emu.run();
	}
	
	private void initGUI() {
	    serial = new SerialPortOutput();
		about = new AboutWindow();
		filechooser = new JFileChooser();
		toolBar = new JToolBar(); 
		cd_list = new jCDChooser();
		BinFilter bin_supported = new BinFilter();
		try {
			setTitle("Esoteric - Because there is no spoon");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			JFrame.setDefaultLookAndFeelDecorated(true);
			{
                JPanel jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setPreferredSize(new java.awt.Dimension(372, 124));
				jPanel1.setSize(383, 125);
				{
                    JToolBar toolBar1 = new JToolBar();
					jPanel1.add(toolBar1);
					toolBar1.setPreferredSize(new java.awt.Dimension(185, 40));
					toolBar1.setFloatable(false);
					toolBar1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
					{
                        JButton jButton1 = new JButton();
						toolBar1.add(jButton1);
						toolBar1.addSeparator(new Dimension(11,0));
						jButton1.setIcon(new ImageIcon("imagens/PlayNormal.png"));
						jButton1.setPressedIcon(new ImageIcon("imagens/PlayPressed.png"));
						jButton1.setToolTipText("Press to Play");
						jButton1.setBounds(185, 30, jButton1.getWidth(), jButton1.getHeight());
						
						jButton1.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
									Loader.loadBinaryFile(selected_file.getPath(),false,Memory.mem,Memory.getMemoryAddress(0x8C000000 + 0x00010000));
									Emu.guiInterface.setVisible(false);
									Emu.start();
									emuLoop();
							}
						});
						
					}
					{
                        JButton jButton2 = new JButton();
						toolBar1.add(jButton2);
						toolBar1.addSeparator(new Dimension(11,0));
						jButton2.setIcon(new ImageIcon("imagens/PauseNormal.png"));
						jButton2.setPressedIcon(new ImageIcon("imagens/PausePressed.png"));
						jButton2.setToolTipText("Press to pause emulation");
					}
					{
                        JButton jButton3 = new JButton();
						toolBar1.add(jButton3);
						toolBar1.addSeparator(new Dimension(11,0));
						jButton3.setIcon(new ImageIcon("imagens/Stop1Normal.png"));
						jButton3.setPressedIcon(new ImageIcon("imagens/Stop1Pressed.png"));
						jButton3.setToolTipText("Press to stop emulation");
						
						jButton3.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
							}
						});
					}
					{
                        JButton jButton4 = new JButton();
						toolBar1.add(jButton4);
						toolBar1.addSeparator(new Dimension(11,0));
						jButton4.setIcon(new ImageIcon("imagens/EjectNormal.png"));
						jButton4.setPressedIcon(new ImageIcon("imagens/EjectPressed.png"));
						jButton4.setToolTipText("Press to exit Esoteric");
						
						jButton4.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
									System.exit(0);
							}
						});
					}
				}
				{
					{
                        JPanel jPanel3 = new JPanel();
						GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
						jPanel3.setLayout(jPanel3Layout);
						jPanel1.add(jPanel3);
						jPanel3.setBounds(0, 12, 369, 86);
						jPanel3.setPreferredSize(new java.awt.Dimension(361, 86));
                        JLabel jLabel1;
                        {
							jLabel1 = new JLabel();
							jLabel1.setText("Application");
							jLabel1.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
							jLabel1.setFont(new java.awt.Font("Andale Mono",3,12));
						}
                        JLabel jLabel2;
                        {
							jLabel2 = new JLabel();
							jLabel2.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
						}
                        JLabel jLabel3;
                        {
							jLabel3 = new JLabel();
							jLabel3.setText("Format");
							jLabel3.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
							jLabel3.setFont(new java.awt.Font("Andale Mono",3,12));
						}
                        JLabel jLabel5;
                        {
							jLabel5 = new JLabel();
							jLabel5.setText("Version");
							jLabel5.setFont(new java.awt.Font("Andale Mono",3,11));
						}
                        JLabel jLabel4;
                        {
							jLabel4 = new JLabel();
							jLabel4.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
						}
						jPanel3Layout.setHorizontalGroup(jPanel3Layout.createSequentialGroup()
							.addGroup(jPanel3Layout.createParallelGroup()
							    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
							    .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
							.addGap(24)
							.addGroup(jPanel3Layout.createParallelGroup()
							    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, 0, 229, Short.MAX_VALUE)
							    .addComponent(jLabel4, GroupLayout.Alignment.LEADING, 0, 229, Short.MAX_VALUE)
							    .addGroup(GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
							        .addGap(133)
							        .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							        .addGap(40)))
							.addContainerGap(16, 16));
						jPanel3Layout.setVerticalGroup(jPanel3Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel3Layout.createParallelGroup()
							    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
							    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(jPanel3Layout.createParallelGroup()
							    .addComponent(jLabel4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
							    .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
					}
				}
			}
			{
                JMenuBar menuBar1 = new JMenuBar();
				setJMenuBar(menuBar1);
				{
                    JMenu jMenu1 = new JMenu();
					menuBar1.add(jMenu1);
					jMenu1.setText("File");
					{
                        JMenuItem openBin = new JMenuItem();
						jMenu1.add(openBin);
						openBin.setText("Open Binary");
						
						filechooser.setFileFilter(bin_supported);
						openBin.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								if(filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
									selected_file = filechooser.getSelectedFile();
									System.out.println("OK " + selected_file.getPath());
								}
							}
						});
						
					}
					{
                        JMenuItem openCD = new JMenuItem();
						jMenu1.add(openCD);
						openCD.setText("Open CD-ROM");
						openCD.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								cd_list.setVisible(true);
							}
						});
					}
				}
				{
                    JMenu debuggingMenu = new JMenu();
					menuBar1.add(debuggingMenu);
					debuggingMenu.setText("Debugging");
					{
                        JMenuItem serialOutput = new JMenuItem();
						debuggingMenu.add(serialOutput);
						serialOutput.setText("Serial Port Output");
						serialOutput.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								serial.setVisible(true);
							}
						});
					}
					{
                        JMenuItem sh4Regs = new JMenuItem();
						debuggingMenu.add(sh4Regs);
						sh4Regs.setText("Sh4 Registers");
					}
				}
				{
					JMenu skins = new JMenu();
					menuBar1.add(skins);
					skins.setText("Skins");
					{
						for(int i =0;i < skin.capacity();i++){
							final int j = i;
							JMenuItem item = new JMenuItem();
							skins.add(item);
							item.setText(skin.get(i).getDisplayName());
							item.addMouseListener(new MouseAdapter(){
								public void mousePressed(java.awt.event.MouseEvent evt) {
									SubstanceLookAndFeel.setSkin(skin.get(j).getClassName());
							}
							});
						}	
					}
				}	
				{
                    JMenu about1 = new JMenu();
					menuBar1.add(about1);
					about1.setText("About");
					{
                        JMenuItem acknowledgments = new JMenuItem();
						about1.add(acknowledgments);
						acknowledgments.setText("Acknowledgments");
						acknowledgments.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								about.setVisible(true);
						}
						});
					}
					{
                        JMenuItem institution = new JMenuItem();
						about1.add(institution);
						institution.setText("U.Evora Homepage");
						institution.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								try {
									BrowserInterface.show(new URI("http://www.uevora.pt"));
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
						}
						});
					}
					{
                        JMenuItem homepage = new JMenuItem();
						about1.add(homepage);
						homepage.setText("Esoteric Homepage");
						homepage.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								try {
									BrowserInterface.show(new URI("http://code.google.com/p/esotericemu"));
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
						}
						});
					}
					{
                        JMenuItem jMenuItem1 = new JMenuItem();
						about1.add(jMenuItem1);
						jMenuItem1.setText("LWJGL Homepage");
						jMenuItem1.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								try {
									BrowserInterface.show(new URI("http://www.lwjgl.org"));
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
						}
						});
					}
					{
                        JMenuItem jMenuItem2 = new JMenuItem();
						about1.add(jMenuItem2);
						jMenuItem2.setText("Java Homepage");
						jMenuItem2.addMouseListener(new MouseAdapter(){
							public void mousePressed(java.awt.event.MouseEvent evt) {
								try {
									BrowserInterface.show(new URI("http://java.sun.com"));
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}
						}
						});
					}
				}
			}
			pack();
			this.setSize(378, 189);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


static class BinFilter extends FileFilter{

	public boolean accept(File binary) {
		return binary.getName().endsWith("bin");
	}

	public String getDescription() {
		return "BIN Files";
	}	
}

}
