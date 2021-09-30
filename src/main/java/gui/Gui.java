package gui;
import io.Loader;
import memory.Memory;
import sh4.Sh4Context;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageProducer;

import javax.swing.JSeparator;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import java.io.File;
import java.nio.file.Paths;
import javax.swing.filechooser.FileFilter;;

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
public class Gui extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	private JMenuItem helpMenuItem;
	private JMenu jMenu5;
	private JMenuItem MemoryRegs;
	private JMenuItem Sh4;
	private JMenu Dissassembler;
	private JMenuItem sh4MenuItem;
	private JMenuItem videoMenuItem;
	private JMenuItem BiosMenuItem;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;
	private final  JFileChooser fileChooser = new JFileChooser();
	
	private final About acerca = new About();
	private final SerialPortOutput serial = new SerialPortOutput();
	private final PathInput pathInput = new PathInput();
	private final MemoryMappedRegDebug mmreg = new MemoryMappedRegDebug();
	private final Sh4Disassembler sh4dis = new Sh4Disassembler();
	private final ProcessorConfiguration processorConfig = new ProcessorConfiguration();
	
	private JMenuItem serialOutput;
	private MyImagePanel  logo;
	
	private final String binDescription = "bin";
	private final String isoDescription = "iso,cue,nrg";

	BinaryFileFilter binFilter = new BinaryFileFilter();
	IsoFileFilter isoFilter = new IsoFileFilter();

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", Paths.get(".", "lib").toAbsolutePath().toString());
		Memory.setUpMemoryZones();
		Loader.loadBinaryFile("res/dc_boot.bin",false,Memory.bios,0);
		Loader.loadBinaryFile("res/dc_flash.bin",false,Memory.flash,0);
//		Loader.loadBinaryFile("ip.bin",false,Memory.mem, Memory.getMemoryAddress(0x8C000000 + 0x00008000));
//		Loader.loadBinaryFile("plasma.bin",false,Memory.mem,Memory.getMemoryAddress(0x8C000000 + 0x00010000));
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				Gui inst = new Gui();
//				inst.setLocationRelativeTo(null);
//				inst.setVisible(true);
//			}
//		});
		Emu test = new Emu();
		Sh4Context.debugging = true;
		long totCycles;
		while(true) {
			test.run();
		}
	}
	
	public Gui() {
		super();
		
		initGUI();
	}
	
	private void initGUI() {
		try {
//			logo = new MyImagePanel(javax.imageio.ImageIO.read(new File("logo.jpg")));
//			getContentPane().add(logo);
			setResizable(false);
			setSize(1000,290);
			setTitle("Esoteric - My empiricist ways are now strong");
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu3.add(newFileMenuItem);
						newFileMenuItem.setText("Open Binary File");
						newFileMenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								newFileMenuItemMouseClicked(evt);
							}
						});
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open Iso File");
						openFileMenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								openFileMenuItemMouseClicked(evt);
							}
						});
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
						exitMenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								exitMenuItemMouseClicked(evt);
							}
						});
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Configuration");
					{
						BiosMenuItem = new JMenuItem();
						jMenu4.add(BiosMenuItem);
						BiosMenuItem.setText("Paths");
						BiosMenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								BiosMenuItemMouseClicked(evt);
							}
						});
					}
					{
						videoMenuItem = new JMenuItem();
						jMenu4.add(videoMenuItem);
						videoMenuItem.setText("Video Configuration");
						videoMenuItem.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								videoMenuItemMouseClicked(evt);
							}
						});
					}
					{
						sh4MenuItem = new JMenuItem();
						jMenu4.add(sh4MenuItem);
						sh4MenuItem.setText("Processor Configuration");
						sh4MenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								sh4MenuItemMouseClicked(evt);
							}
						});
					}
				}
				{
					Dissassembler = new JMenu();
					jMenuBar1.add(Dissassembler);
					Dissassembler.setText("Debugger");
					{
						Sh4 = new JMenuItem();
						Dissassembler.add(Sh4);
						Sh4.setText("Sh4 Dissassembler");
						Sh4.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								Sh4MouseClicked(evt);
							}
						});
					}
					{
						MemoryRegs = new JMenuItem();
						Dissassembler.add(MemoryRegs);
						MemoryRegs.setText("Memory Mapped Regs");
						MemoryRegs.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								MemoryRegsMouseClicked(evt);
							}
						});
					}
					{
						serialOutput = new JMenuItem();
						Dissassembler.add(serialOutput);
						serialOutput.setText("SCFI OUTPUT");
						serialOutput.addMouseListener(new MouseAdapter(){
							public void mousePressed(MouseEvent evt) {
								serialOutputMouseClicked(evt);
							}
						});
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("About");
						helpMenuItem.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent evt) {
								helpMenuItemMouseClicked(evt);
							}
						});
					}
				}
			}
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setMultiSelectionEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void newFileMenuItemMouseClicked(MouseEvent evt) {
		int returnVal = fileChooser.showOpenDialog(this);
		fileChooser.setFileFilter(binFilter);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		}
	}
	
	private void openFileMenuItemMouseClicked(MouseEvent evt) {
		int returnVal = fileChooser.showOpenDialog(this);
		fileChooser.setFileFilter(isoFilter);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		}
	}
	
	private void exitMenuItemMouseClicked(MouseEvent evt) {
		System.exit(0);
	}
	
	private void BiosMenuItemMouseClicked(MouseEvent evt) {
		System.out.println("BiosMenuItem.mouseClicked, event="+evt);
		pathInput.setVisible(true);
	}
	
	private void videoMenuItemMouseClicked(MouseEvent evt) {
		System.out.println("videoMenuItem.mouseClicked, event="+evt);
		//TODO add your code for videoMenuItem.mouseClicked
	}
	
	private void sh4MenuItemMouseClicked(MouseEvent evt) {
		System.out.println("sh4MenuItem.mouseClicked, event="+evt);
		processorConfig.setVisible(true);
	}
	
	private void Sh4MouseClicked(MouseEvent evt) {
		System.out.println("Sh4.mouseClicked, event="+evt);
		sh4dis.setVisible(true);
	}
	
	private void MemoryRegsMouseClicked(MouseEvent evt) {
		System.out.println("MemoryRegs.mouseClicked, event="+evt);
		mmreg.setVisible(true);
	}
	
	private void helpMenuItemMouseClicked(MouseEvent evt) {
		acerca.setVisible(true);
	}
	
	private void serialOutputMouseClicked(MouseEvent evt) {
		serial.setVisible(true);		
	}

	class BinaryFileFilter extends FileFilter{

		public boolean accept(File pathname) {
			return pathname.getPath().endsWith("bin");
		}

		public String getDescription() {
			return binDescription;
		}
	}
	
	class IsoFileFilter extends FileFilter{

		public boolean accept(File pathname) {
			String x = pathname.getPath().substring(pathname.getPath().length()-3);
			return (x.equalsIgnoreCase("cue") || x.equalsIgnoreCase("nrg") || x.equalsIgnoreCase("iso"));
		}

		public String getDescription() {
			return isoDescription;
		}
		
	}
}
