import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class of the main window
 * @author Lin, Xiaofeng
 *
 */
public class MainWindow extends JFrame {

	private JButton stock, shelf, sell;
	private JPanel mainPanel, buttonPanel;
	private JLabel imageLabel;
	
	/**
	 * Default constructor
	 */
	public MainWindow() {
		setTitle("Inventory System");
		setSize(450, 200);
		setLocation(400,300);
		createStock();
		createShelf();
		createSell();
		createPanel();
	}
	
	/**
	 * creates the stock button which leads to the stock interface when pressed
	 */
	public void createStock() {
		stock = new JButton("Add to Stock");
		class StockListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				StockWindow stockWindow = new StockWindow();
				stockWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				stockWindow.setVisible(true);
				setEnabled(false);
				stockWindow.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
			}
		}
		ActionListener stockListener = new StockListener();
		stock.addActionListener(stockListener);
	}
	
	/**
	 * creates the shelf button which leads to the shelf interface when pressed
	 */
	public void createShelf() {
		shelf = new JButton("Move to Shelf");
		shelf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ShelfWindow shelfWindow = new ShelfWindow();
				shelfWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				shelfWindow.setVisible(true);
				setEnabled(false);
				shelfWindow.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
			}
		});
	}
	
	/**
	 * creates the sell button which leads to the sell interface when pressed
	 */
	public void createSell() {
		sell = new JButton("Sell");
		sell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SellWindow sellWindow = new SellWindow();
				sellWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				sellWindow.setVisible(true);
				setEnabled(false);
				sellWindow.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
			}
		});
	}
	
	/**
	 * for the layout of all components
	 */
	public void createPanel() {
		mainPanel = new JPanel();
		imageLabel = new JLabel("Inventory System");
		imageLabel.setFont(new Font("Serif", Font.PLAIN, 50));
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		buttonPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		buttonPanel.add(stock);
		buttonPanel.add(shelf);
		buttonPanel.add(sell);
		mainPanel.add(imageLabel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(mainPanel);
	}
}
