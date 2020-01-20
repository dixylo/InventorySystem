/**
 * Class for brand name items
 * @author Lin, Xiaofeng
 *
 */
public class Brand extends Generic {
	
	private String brandName;
	
	/**
	 * Default constructor
	 */
	public Brand() {
		brandName = null;
	}
	
	/**
	 * Sets the brand name of an item
	 * @param brandName
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	/**
	 * Gets the brand name of an item
	 * @return brand name
	 */
	public String getBrandName() {
		return this.brandName;
	}
}
