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
 * class for the shelf window
 * @author Lin, Xiaofeng
 *
 */
public class ShelfWindow extends JFrame {
	private JButton add, show, cancel;
	private JPanel basePanel, operatePanel, quantityPanel, buttonPanel, lowerTablePanel;
	private JLabel promptLabel;
	private JTable stockData, showSelectedItem;
	private DefaultTableModel tableModel;
	private JTextField quantityText;
	private JScrollPane scrollPaneUpper;
	private String barCode, quantity;
	public static final int NUM_ATTR = 6;
	public static final String STOCK_PATH = "stock.txt";
	public static final String SHELF_PATH = "shelf.txt";
	
	/**
	 * default constructor
	 */
	public ShelfWindow() {
		setTitle("Shelf Window");
		setSize(800, 300);
		setLocation(400, 300);
		createTable();
		createAdd();
		createShow();
		createCancel();
		createPanel();
	}
	
	/**
	 * creates the table that shows the data of items off the shelf
	 */
	public void createTable() {
		File stock = new File(STOCK_PATH);
		String[] columnNames = {"Barcode Number", "Product Name", "Brand Name", "Expire Date", "Unit Price", "Quantity"};
		stockData = new JTable();
		String[] columnNamesLower = {"Barcode Number", "Product Name", "Brand Name", "Expire Date", "Unit Price"};
		String[][] selectedItem = new String[1][NUM_ATTR - 1];
		showSelectedItem = new JTable(selectedItem, columnNamesLower);
		stockData.setAutoCreateRowSorter(true);
		scrollPaneUpper = new JScrollPane(stockData);
		
		stockData.setFillsViewportHeight(true);
		tableModel = new DefaultTableModel(0, 6);
		tableModel.setColumnIdentifiers(columnNames);
		stockData.setModel(tableModel);
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(stock));
			while((line = reader.readLine()) != null) 
	        {
	           tableModel.addRow(line.split(", ")); 
	        }
	        reader.close();
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error");
			ioe.printStackTrace();
		}
		
		stockData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int[] rows = stockData.getSelectedRows();
					for (int row : rows) {
						for (int col = 0; col < NUM_ATTR - 1; col++) {
							selectedItem[0][col] = (String) stockData.getValueAt(row, col);
							showSelectedItem.setValueAt(selectedItem[0][col], 0, col);
						}
					}
					promptLabel.setText("Item Selected as Below. Please Enter Quantity: ");
				}
			}
		});
	}
	
	/**
	 * create the "move to shelf" button
	 */
	public void createAdd() {
		add = new JButton("Move to Shelf");
		class AddListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				DataModifier check = new DataModifier();
				barCode = (String) showSelectedItem.getValueAt(0, 0);
				quantity = quantityText.getText();
				if (check.itemExists(SHELF_PATH, barCode)) {
					check.modifyQuantity(SHELF_PATH, barCode, quantity);
					check.modifyQuantity(STOCK_PATH, barCode, "-" + quantity);
					promptLabel.setText("Item Replenished!");
				} else {
					BufferedWriter bufferedWriter = null;
					FileWriter fileWriter = null;
					try {
						File shelf = new File(SHELF_PATH);
						if (!shelf.exists()) {
							shelf.createNewFile();
						}
						fileWriter = new FileWriter(shelf.getAbsoluteFile(), true);
						bufferedWriter = new BufferedWriter(fileWriter);
						String lineToAdd = "";
						for (int i =0; i < NUM_ATTR -1; i++) {
							lineToAdd += ((String) showSelectedItem.getValueAt(0, i)) + ", ";
						}
						if (Integer.parseInt(quantityText.getText()) <= Integer.parseInt((String) stockData.getValueAt(0, NUM_ATTR - 1))) {
							lineToAdd += quantityText.getText();
						} else {
							promptLabel.setText("No sufficient Items in Stock. Please Reset Quantity.");
							return;
						}
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
					check.modifyQuantity(STOCK_PATH, barCode, "-" + quantity);
					promptLabel.setText("Item Added Successfully!");
				}
				quantityText.setText("");
			}
		}
		ActionListener addListener = new AddListener();
		add.addActionListener(addListener);
	}
	
	/**
	 * creates the "show and modify items off the shelf" button
	 */
	public void createShow() {
		show = new JButton("Show & Modify Items off the Shelf");
		class ShowListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				ShelfTable shelfTable = new ShelfTable();
				shelfTable.setVisible(true);
				shelfTable.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setEnabled(false);
				shelfTable.addWindowListener(new WindowAdapter() {
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
	 * close the current window
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
		operatePanel = new JPanel();
		quantityPanel = new JPanel();
		buttonPanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		operatePanel.setLayout(new GridLayout(3, 1));
		promptLabel = new JLabel("Select an Item and Enter Quantity: ");
		quantityText = new JTextField(5);
		quantityPanel.add(promptLabel);
		quantityPanel.add(quantityText);
		buttonPanel.add(add);
		buttonPanel.add(show);
		buttonPanel.add(cancel);
		operatePanel.add(quantityPanel);
		lowerTablePanel = new JPanel(new BorderLayout());
		lowerTablePanel.add(showSelectedItem, BorderLayout.CENTER);
		lowerTablePanel.add(showSelectedItem.getTableHeader(), BorderLayout.NORTH);
		operatePanel.add(lowerTablePanel);
		operatePanel.add(buttonPanel);
		basePanel.add(scrollPaneUpper, BorderLayout.CENTER);
		basePanel.add(operatePanel, BorderLayout.SOUTH);
		add(basePanel);
	}
	
	
}
