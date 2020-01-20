import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * class for displaying the statistics
 * @author Lin, Xiaofeng
 *
 */
public class StatsTable extends JFrame {
	
	private JTable sellData;
	private JPanel basePanel, buttonPanel;
	private JScrollPane scrollPane;
	private JButton compare, cancel;
	private DefaultTableModel tableModel;
	public static final int NUM_ATTR = 6;
	public static final String SELL_PATH = "sell.txt";
	
	/**
	 * default constructor
	 */
	public StatsTable() {
		setTitle("Statistics");
		setSize(800, 200);
		setLocation(400,300);
		createCompare();
		createCancel();
		createPanel();
	}
	
	/**
	 * compare generic and brand items
	 */
	public void createCompare() {
		compare = new JButton("Compare Generic & Brand");
		String[] columnNames = {"Product Name", "Brand Name", "Unit Price ($)", "Quantity", "Total ($)"};
		sellData = new JTable();
		sellData.setAutoCreateRowSorter(true);
		scrollPane = new JScrollPane(sellData);
		sellData.setFillsViewportHeight(true);
		tableModel = new DefaultTableModel(0, 5);
		tableModel.setColumnIdentifiers(columnNames);
		sellData.setModel(tableModel);
		
		
		compare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File sell = new File(SELL_PATH);
				String line;
				String[] value = new String[NUM_ATTR];
				BufferedReader reader;
				Generic g = new Generic();
				Brand b = new Brand();
				ArrayList<Generic> gl = new ArrayList<Generic>();
				ArrayList<Brand> bl = new ArrayList<Brand>();
				String lineG = "";
				String lineB = "";
				try {
					reader = new BufferedReader(new FileReader(sell));
					while((line = reader.readLine()) != null) {
						int i = 0;
						int indexComma = 0;
						int countComma = 0;
						while (countComma < NUM_ATTR -1) {
							if (line.charAt(i) == ',') {
								value[countComma] = line.substring(indexComma, i);
								indexComma = i + 2;
								countComma++;
							}
							i++;
						}
						value[NUM_ATTR - 1] = line.substring(indexComma);
						if (value[2].equals("")) {
							g.setBarcodeNumber(value[0]);
							g.setProductName(value[1]);
							g.setExpireDate(value[3]);
							g.setPrice(value[4]);
							g.setQuantity(value[5]);
							gl.add(g);
						} else {
							b.setBarcodeNumber(value[0]);
							b.setProductName(value[1]);
							b.setBrandName(value[2]);
							b.setExpireDate(value[3]);
							b.setPrice(value[4]);
							b.setQuantity(value[5]);
							bl.add(b);
						}
						 
					}
					for (int i = 0; i < gl.size(); i++) {
						for (int j = 0; j < bl.size(); j++) {
							if (gl.get(i).getProductName().equals(bl.get(j).getProductName())) {
								lineG = gl.get(i).getProductName() + ", Generic, " + gl.get(i).getPrice() + ", " + gl.get(i).getQuantity() + ", " +
										Double.toString(Double.parseDouble(gl.get(i).getPrice()) * Double.parseDouble(gl.get(i).getQuantity()));
								lineB = bl.get(j).getProductName() + ", " + bl.get(j).getBrandName() + ", " + bl.get(j).getPrice() + ", " + bl.get(j).getQuantity() + ", " +
										Double.toString(Double.parseDouble(bl.get(j).getPrice()) * Double.parseDouble(bl.get(j).getQuantity()));
								tableModel.addRow(lineG.split(", "));
								tableModel.addRow(lineB.split(", "));
							}
						}
					}
					reader.close();
				} catch(IOException ioe) {
					JOptionPane.showMessageDialog(null, "Error");
					ioe.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * close the current window
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
		buttonPanel.add(compare);
		buttonPanel.add(cancel);
		basePanel.setLayout(new BorderLayout());
		basePanel.add(scrollPane, BorderLayout.CENTER);
		basePanel.add(buttonPanel, BorderLayout.SOUTH);
		add(basePanel);
	}
}
