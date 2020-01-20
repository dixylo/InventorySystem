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
 * class for the shelf data displaying
 * @author Lin, Xiaofeng
 *
 */
public class ShelfTable extends JFrame {
	private JTable shelfData;
	private JPanel basePanel, buttonPanel;
	private JScrollPane scrollPane;
	private JButton delete, cancel;
	private DefaultTableModel tableModel;
	public static final int NUM_ATTR = 6;
	public static final String SHELF_PATH = "shelf.txt";
	
	/**
	 * default constructor
	 */
	public ShelfTable() {
		setTitle("Items off the Shelf");
		setSize(800, 200);
		setLocation(400,300);
		
		createTable();
		createDelete();
		createCancel();
		createPanel();
	}
	
	/**
	 * creates the table that shows data of items off the shelf
	 */
	public void createTable() {
		File shelf = new File(SHELF_PATH);
		String[] columnNames = {"Barcode Number", "Product Name", "Brand Name", "Expire Date", "Unit Price", "Quantity"};
		shelfData = new JTable();
		shelfData.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(shelfData);
		shelfData.setFillsViewportHeight(true);
		tableModel = new DefaultTableModel(0, 6);
		tableModel.setColumnIdentifiers(columnNames);
		shelfData.setModel(tableModel);
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(shelf));
			while((line = reader.readLine()) != null) 
	        {
	           tableModel.addRow(line.split(", ")); 
	        }
	        reader.close();
		} catch(IOException ioe) {
			JOptionPane.showMessageDialog(null, "Error");
			ioe.printStackTrace();
		}
		
		shelfData.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				String lineToChange = "";
				for (int i = 0; i < NUM_ATTR; i++) {
					if (i == NUM_ATTR - 1) {
						lineToChange += (String) shelfData.getValueAt(row, i);
					} else {
						lineToChange += ((String) shelfData.getValueAt(row, i)) + ", ";
					}
				}
				
				DataModifier changeRow = new DataModifier();
				changeRow.changeLineFromFile(SHELF_PATH, row, lineToChange);
			}
		});
	}
	
	/**
	 * removes a selected item from the table
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
	 * deletes an item
	 */
	public void createDelete() {
		delete = new JButton("Delete Selected Rows");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataModifier removeRow = new DataModifier();
				int[] rows = shelfData.getSelectedRows();
				String lineToRemove = "";
				for (int row : rows) {
					for (int i = 0; i < NUM_ATTR; i++) {
						if (i == NUM_ATTR - 1) {
							lineToRemove += (String) shelfData.getValueAt(row, i);
						} else {
							lineToRemove += ((String) shelfData.getValueAt(row, i)) + ", ";
						}
					}
					removeRow.removeLineFromFile(SHELF_PATH, lineToRemove);
				}
				removeSelectedFromTable(shelfData);
			}
		});
	}
	
	/**
	 * cancel the current window
	 */
	public void createCancel() {
		cancel = new JButton("Back to Shelf Window");
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
