package gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public final class BrowserInterface {

	static String default_browser="firefox";
	
	static Desktop desktop;
	private static action browser;
	
	
	static{
		if(Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
			if(desktop.isSupported(Desktop.Action.BROWSE)) {
				browser = new DefaultBrowser();
			}
		}
		browser = new ManualBrowser();
	}
	
	public static void show(URI link){
		browser.open(link);
	}
		
	static class ManualBrowser implements action{

		public void open(URI link) {
			try {
				Runtime.getRuntime().exec(new String[] {default_browser,link.toString()});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	static class DefaultBrowser implements action{

		@Override
		public void open(URI link) {
			try {
				desktop.browse(link);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	private interface action{
		 void open(URI link);
	}
}
