package imagingApp;
import javax.swing.SwingUtilities;

import gui.*;

public class Main {

	public static void main(String[] args) {
	
		SwingUtilities.invokeLater(Main::startApp);
	}

	private static void startApp() {
		new Window("Image Editing").setVisible(true);
	}
}
