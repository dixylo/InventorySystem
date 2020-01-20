import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * class for constructing a sell interface
 * @author Lin, Xiaofeng
 *
 */
public class SellWindow extends JFrame {
	private JButton addToCart, checkout, show, stats, cancel;
	private JPanel basePanel, inputPanel, textPanel, promptPanel, buttonPanel;
	private JLabel barcodeLabel, quantityLabel, promptLabel;
	private JTable payTable;
	private DefaultTableModel tableModel;
	private JTextField barcodeText, quantityText;
	private JScrollPane scrollPane;
	private String barcode, quantity;
	public static final int NUM_ATTR = 6;
	public static final String SHELF_PATH = "shelf.txt";
	public static final String SELL_PATH = "sell.txt";
	
	/**
	 * default constructor
	 */
	public SellWindow() {
		setTitle("Sell Window");
		setSize(800, 300);
		setLocation(400, 300);
		//createTable();
		createAddToCart();
		createCheckout();
		createShow();
		createStats();
		createCancel();
		createPanel();
	}
	
	/**
	 * creates the add to cart button
	 */
	public void createAddToCart() {
		addToCart = new JButton("Add to Cart");
		String[] columnNames = {"Barcode Number", "Product Name", "Brand Name", "Expire Date", "Unit Price", "Quantity"};
		payTable = new JTable();
		payTable.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(payTable);
		payTable.setFillsViewportHeight(true);
		tableModel = new DefaultTableModel(0, 6);
		tableModel.setColumnIdentifiers(columnNames);
		payTable.setModel(tableModel);
		
		class AddListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				DataModifier check = new DataModifier();
				barcode = barcodeText.getText().trim();
				quantity = quantityText.getText();
				String line = "";
				if (check.itemExists(SHELF_PATH, barcode)) {
					line = check.getItemByBarcode(SHELF_PATH, barcode) + " " + quantity;
					tableModel.addRow(line.split(", "));
					promptLabel.setText("Item Added to Cart");
					
				} else {
					promptLabel.setText("This Item is NOT off the Shelf! Please Check Barcode.");
				}
				barcodeText.setText("");
				quantityText.setText("1");
			}
		}
		ActionListener addListener = new AddListener();
		addToCart.addActionListener(addListener);
	}
	
	/**
	 * creates the checkout button
	 */
	public void createCheckout() {
		checkout = new JButton("Checkout");
		
		class AddListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				DataModifier check = new DataModifier();
				double p;
				double q;
				double total = 0;
				for (int i = 0; i < tableModel.getRowCount(); i++) {
					p = Double.parseDouble((String) tableModel.getValueAt(i, 4));
					q = Double.parseDouble((String) tableModel.getValueAt(i, 5));
					total += p * q;
				}
				promptLabel.setText("Total: $" + Double.toString(total));

				for (int i = 0; i < tableModel.getRowCount(); i++) {
					barcode = (String) tableModel.getValueAt(i, 0);
					quantity = (String) tableModel.getValueAt(i, 5);
					if (check.itemExists(SELL_PATH, barcode)) {
						check.modifyQuantity(SELL_PATH, barcode, quantity);
						check.modifyQuantity(SHELF_PATH, barcode, "-" + quantity);
					} else {
						BufferedWriter bufferedWriter = null;
						FileWriter fileWriter = null;
						try {
							File sell = new File(SELL_PATH);
							if (!sell.exists()) {
								sell.createNewFile();
							}
							fileWriter = new FileWriter(sell.getAbsoluteFile(), true);
							bufferedWriter = new BufferedWriter(fileWriter);
							String lineToAdd = "";
							for (int j =0; j < NUM_ATTR - 1; j++) {
								lineToAdd += ((String) tableModel.getValueAt(i, j)) + ", ";
							}
							lineToAdd += quantity;
							
							bufferedWriter.write(lineToAdd);
							bufferedWriter.newLine();
							
						} catch(IOException e1) {
							e1.printStackTrace();
						} finally {
							try {
								if (bufferedWriter != null) {
									bufferedWriter.close();
								}
								if (fileWriter != null) {
									fileWriter.close();
								}
							} catch(IOException e2) {
								e2.printStackTrace();
							}
						}
						check.modifyQuantity(SHELF_PATH, barcode, "-" + quantity);
					}
				}
			}
		}
		ActionListener addListener = new AddListener();
		checkout.addActionListener(addListener);
	}
	
	/**
	 * creates the "show and modify item sold" button
	 */
	public void createShow() {
		show = new JButton("Show & Modify Items Sold");
		class ShowListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				SellTable sellTable = new SellTable();
				sellTable.setVisible(true);
				sellTable.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setEnabled(false);
				sellTable.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
			}
		}
		ActionListener showListener = new ShowListener();
		show.addActionListener(showListener);
	}
	
	/**
	 * creates the statistics button
	 */
	public void createStats() {
		stats = new JButton("Show Statistics");
		class StatsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				StatsTable statsTable = new StatsTable();
				statsTable.setVisible(true);
				statsTable.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setEnabled(false);
				statsTable.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
			}
		}
		ActionListener statsListener = new StatsListener();
		stats.addActionListener(statsListener);
	}
	
	/** 
	 * creates the back to main button
	 */
	public void createCancel() {
		cancel = new JButton("Back to Main");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	/**
	 * for the layout of the window
	 */
	public void createPanel() {
		basePanel = new JPanel();
		inputPanel = new JPanel();
		textPanel = new JPanel();
		promptPanel = new JPanel();
		buttonPanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		inputPanel.setLayout(new BorderLayout());
		textPanel.setLayout(new GridLayout(2, 2));
		promptPanel.setLayout(new GridLayout(2, 1));
		barcodeLabel = new JLabel("Barcode Number");
		quantityLabel = new JLabel("Quantity");
		promptLabel = new JLabel("Select an Item and Enter Quantity.");
		promptLabel.setHorizontalAlignment(JLabel.CENTER);
		barcodeText = new JTextField(5);
		quantityText = new JTextField(5);
		quantityText.setText("1");
		textPanel.add(barcodeLabel);
		textPanel.add(quantityLabel);
		textPanel.add(barcodeText);
		textPanel.add(quantityText);
		promptPanel.add(promptLabel);
		promptPanel.add(addToCart);
		inputPanel.add(textPanel, BorderLayout.CENTER);
		inputPanel.add(promptPanel, BorderLayout.SOUTH);
		buttonPanel.add(checkout);
		buttonPanel.add(show);
		buttonPanel.add(stats);
		buttonPanel.add(cancel);
		basePanel.add(inputPanel, BorderLayout.NORTH);
		basePanel.add(scrollPane, BorderLayout.CENTER);
		basePanel.add(buttonPanel, BorderLayout.SOUTH);
		add(basePanel);
	}
	
	
}
