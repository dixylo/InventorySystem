import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class for the window of items in stock
 * @author Lin, Xiaofeng
 *
 */
public class StockWindow extends JFrame {
	private JButton add, show, cancel;
	private JPanel basePanel, inputPanel, buttonPanel, operatePanel;
	private JLabel barCodeLabel, productNameLabel, brandLabel, quantityLabel, priceLabel, expireDateLabel, promptLabel;
	private JTextField barCodeText, productNameText, brandText, quantityText, priceText, expireDateText;
	public static final String STOCK_PATH = "stock.txt";
	
	/**
	 * Default constructor
	 */
	public StockWindow() {
		setTitle("Stock Window");
		setSize(500, 400);
		setLocation(300, 200);
		createAdd();
		createShow();
		createCancel();
		createPanel();
	}
	
	/**
	 * Creates "Add an item to stock" button
	 */
	public void createAdd() {
		add = new JButton("Add an Item");
		class AddListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				DataModifier check = new DataModifier();
				if (check.itemExists(STOCK_PATH, barCodeText.getText())) {
					check.modifyQuantity(STOCK_PATH, barCodeText.getText(), quantityText.getText());
					promptLabel.setText("Item Replenished!");
				} else {
					BufferedWriter bufferedWriter = null;
					FileWriter fileWriter = null;
					try {
						File stock = new File(STOCK_PATH);
						if (!stock.exists()) {
							stock.createNewFile();
						}
						fileWriter = new FileWriter(stock.getAbsoluteFile(), true);
						bufferedWriter = new BufferedWriter(fileWriter);
						bufferedWriter.write(barCodeText.getText() + ", ");
						bufferedWriter.write(productNameText.getText() + ", ");
						bufferedWriter.write(brandText.getText() + ", ");
						bufferedWriter.write(expireDateText.getText() + ", ");
						bufferedWriter.write(priceText.getText() + ", ");
						bufferedWriter.write(quantityText.getText());
						bufferedWriter.newLine();
						promptLabel.setText("New Item Added Successfully!");
						barCodeText.setText("");
						productNameText.setText("");
						brandText.setText("");
						expireDateText.setText("");
						priceText.setText("");
						quantityText.setText("");
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
				}
			}
		}
		ActionListener addListener = new AddListener();
		add.addActionListener(addListener);
	}
	
	/**
	 * Creates "show and modigy items in stock" button
	 */
	public void createShow() {
		show = new JButton("Show & Modify Items in Stock");
		class ShowListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				StockTable stockTable = new StockTable();
				stockTable.setVisible(true);
				stockTable.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				setEnabled(false);
				stockTable.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						setEnabled(true);
					}
				});
				
			}
		}
		ActionListener showListener = new ShowListener();
		show.addActionListener(showListener);
	}
	
	public void createCancel() {
		cancel = new JButton("Back to Main");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	/**
	 * creates all panels used
	 */
	public void createPanel() {
		basePanel = new JPanel();
		inputPanel = new JPanel();
		buttonPanel = new JPanel();
		operatePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		inputPanel.setLayout(new GridLayout(6, 2));
		operatePanel.setLayout(new BorderLayout());
		barCodeLabel = new JLabel("Barcode Number");
		productNameLabel = new JLabel("Product Name");
		brandLabel = new JLabel("Brand Name");
		expireDateLabel = new JLabel("Expire Date");
		priceLabel = new JLabel("Unit Price");
		quantityLabel = new JLabel("Quantity");
		promptLabel = new JLabel("Press a Button");
		promptLabel.setHorizontalAlignment(JLabel.CENTER);
		barCodeText = new JTextField();
		productNameText = new JTextField();
		brandText = new JTextField();
		expireDateText = new JTextField();
		priceText = new JTextField();
		quantityText = new JTextField();
		inputPanel.add(barCodeLabel);
		inputPanel.add(productNameLabel);
		inputPanel.add(barCodeText);
		inputPanel.add(productNameText);
		inputPanel.add(brandLabel);
		inputPanel.add(expireDateLabel);
		inputPanel.add(brandText);
		inputPanel.add(expireDateText);
		inputPanel.add(priceLabel);
		inputPanel.add(quantityLabel);
		inputPanel.add(priceText);
		inputPanel.add(quantityText);
		buttonPanel.add(add);
		buttonPanel.add(show);
		buttonPanel.add(cancel);
		operatePanel.add(buttonPanel, BorderLayout.CENTER);
		operatePanel.add(promptLabel, BorderLayout.SOUTH);
		basePanel.add(inputPanel, BorderLayout.CENTER);
		basePanel.add(operatePanel, BorderLayout.SOUTH);
		add(basePanel);
	}
}
