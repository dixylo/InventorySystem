/**
 * Class for generic items
 * @author Lin, Xiaofeng
 *
 */
public class Generic {
	private String barcodeNumber;
	private String productName;
	private String expireDate;
	private String price;
	private String quantity;
	
	/**
	 * Default constructor
	 */
	public Generic() {
		barcodeNumber = null;
		productName = null;
		expireDate = null;
		price = null;
		quantity = null;
		
	}
	
	/**
	 * Constructor with parameters
	 * @param barcodeNumber
	 * @param productName
	 * @param expireDate
	 * @param price
	 * @param quantity
	 */
	public Generic(String barcodeNumber, String productName, String expireDate, String price, String quantity) {
		this.barcodeNumber = barcodeNumber;
		this.productName = productName;
		this.expireDate = expireDate;
		this.price = price;
		this.quantity = quantity;
		
	}
	
	/**
	 * Sets the barcode number of an item
	 * @param barcodeNumber
	 */
	public void setBarcodeNumber(String barcodeNumber) {
		this.barcodeNumber = barcodeNumber;
	}
	
	/**
	 * Gets the barcode number of an item
	 * @return the barcode number of an item
	 */
	public String getBarcodeNumber() {
		return this.barcodeNumber;
	}
	
	/**
	 * Sets the product name of an item
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
	 * Gets the product name of an item
	 * @return the product name of an item
	 */
	public String getProductName() {
		return this.productName;
	}
	
	/**
	 * Sets the expire date of an item
	 * @param expireDate
	 */
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	
	/**
	 * Gets the expire date of an item
	 * @return the expire date of an item
	 */
	public String getExpireDate() {
		return this.expireDate;
	}
	
	/**
	 * Sets the unit price of an item
	 * @param price
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	/**
	 * Gets the unit price of an item
	 * @return the unit price of an item
	 */
	public String getPrice() {
		return this.price;
	}

	/**
	 * Sets the quantity of an item
	 * @param quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the quantity of an item
	 * @return the quantity of an item
	 */
	public String getQuantity() {
		return this.quantity;
	}
}
