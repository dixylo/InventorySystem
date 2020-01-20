import javax.swing.JFrame;

/**
 * class of the main interface
 * @author Lin, Xiaofeng
 *
 */
public class InventorySystem {

	public static void main(String[] args) {
		// constructs the main window
		JFrame mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);

	}

}
