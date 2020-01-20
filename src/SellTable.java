import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * class for displaying selling data
 * @author Lin, Xiaofeng
 *
 */
public class SellTable extends JFrame {
	private JTable sellData;
	private JPanel basePanel, buttonPanel;
	private JScrollPane scrollPane;
	private JButton delete, cancel;
	private DefaultTableModel tableModel;
	public static final int NUM_ATTR = 6;
	public static final String SHELF_PATH = "shelf.txt";
	public static final String SELL_PATH = "sell.txt";
	
	/**
	 * default constructor
	 */
	public SellTable() {
		setTitle("Items Sold");
		setSize(800, 200);
		setLocation(400,300);
		
		createTable();
		createDelete();
		createCancel();
		createPanel();
	}
	
	/**
	 * creates the table that show the selling data
	 */
	public void createTable() {
		File sell = new File(SELL_PATH);
		String[] columnNames = {"Barcode Number", "Product Name", "Brand Name", "Expire Date", "Unit Price", "Quantity"};
		sellData = new JTable();
		sellData.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(sellData);
		sellData.setFillsViewportHeight(true);
		tableModel = new DefaultTableModel(0, 6);
		tableModel.setColumnIdentifiers(columnNames);
		sellData.setModel(tableModel);
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(sell));
			while((line = reader.readLine()) != null) 
	        {
	           tableModel.addRow(line.split(", ")); 
	        }
	        reader.close();
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error");
			ioe.printStackTrace();
		}
		
		sellData.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				String lineToChange = "";
				for (int i = 0; i < NUM_ATTR; i++) {
					if (i == NUM_ATTR - 1) {
						lineToChange += (String) sellData.getValueAt(row, i);
					} else {
						lineToChange += ((String) sellData.getValueAt(row, i)) + ", ";
					}
				}
				
				DataModifier changeRow = new DataModifier();
				changeRow.changeLineFromFile(SELL_PATH, row, lineToChange);
			}
		});
	}
	
	/**
	 * removes the seleted item from the table
	 * @param table
	 */
	public void removeSelectedFromTable(JTable table) {
		int[] rows = table.getSelectedRows();
		tableModel = (DefaultTableModel) table.getModel();
		for (int row : rows) {
			tableModel.removeRow(table.convertRowIndexToModel(row));
		}
		table.clearSelection();
	}
	
	/**
	 * deletes an item from the table
	 */
	public void createDelete() {
		delete = new JButton("Delete Selected Rows");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataModifier removeRow = new DataModifier();
				int[] rows = sellData.getSelectedRows();
				String lineToRemove = "";
				for (int row : rows) {
					for (int i = 0; i < NUM_ATTR; i++) {
						if (i == NUM_ATTR - 1) {
							lineToRemove += (String) sellData.getValueAt(row, i);
						} else {
							lineToRemove += ((String) sellData.getValueAt(row, i)) + ", ";
						}
					}
					removeRow.removeLineFromFile(SELL_PATH, lineToRemove);
				}
				removeSelectedFromTable(sellData);
			}
		});
	}
	
	/**
	 * cancel the current window
	 */
	public void createCancel() {
		cancel = new JButton("Back to Sell Window");
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
		buttonPanel = new JPanel();
		buttonPanel.add(delete);
		buttonPanel.add(cancel);
		basePanel.setLayout(new BorderLayout());
		basePanel.add(scrollPane, BorderLayout.CENTER);
		basePanel.add(buttonPanel, BorderLayout.SOUTH);
		add(basePanel);
	}
}
