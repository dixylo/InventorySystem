import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for modifying saved data 
 * @author Lin, Xiaofeng
 *
 */
public class DataModifier {
	
	public static final int NUM_ATTR = 6;
	
	/**
	 * checks that a item exists.
	 * @param file
	 * @param barcode
	 * @return
	 */
	public boolean itemExists(String file, String barcode) {
		boolean itemExists = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line ;
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith(barcode)) {
					itemExists = true;
				}
			}
			br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return itemExists;
	}
	
	/**
	 * modifies only the quantity of an item
	 * @param file
	 * @param barcode
	 * @param quantity
	 */
	public void modifyQuantity(String file, String barcode, String quantity) {
		try {
			File inFile = new File(file);
			//Construct the new file that will later be renamed to the original filename. 
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			String line ;
			//Read from the original file and write to the new 
			//unless content matches data to be removed.

			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith(barcode)) {
					int countComa = 0;
					int j = 0;
					while (countComa < NUM_ATTR -1) {
						if (line.charAt(j) == ',') {
							countComa += 1;
						}
						j++;
					}					
					line = line.substring(0, j) + " " + Integer.toString(Integer.parseInt(line.substring(j).trim()) + Integer.parseInt(quantity.trim()));;
				}
				pw.println(line);
				pw.flush();
			}
			pw.close();
			br.close();
			
			//Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}
			//Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * retrieve an item record by barcode
	 * @param file
	 * @param barcode
	 * @return
	 */
	public String getItemByBarcode(String file, String barcode) {
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.trim().startsWith(barcode)) {
					int countComma = 0;
					int i = 0;
					while (countComma < NUM_ATTR -1) {
						if (line.charAt(i) == ',') {
							countComma += 1;
						}
						i++;
					}
					line = line.substring(0, i);
					break;
				}
			}
			br.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return line;
	}
	
	/**
	 * removes an item record
	 * @param file
	 * @param lineToRemove
	 */
	public void removeLineFromFile(String file, String lineToRemove) {
		try {
			File inFile = new File(file);
			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}
			//Construct the new file that will later be renamed to the original filename. 
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			String line ;
			//Read from the original file and write to the new 
			//unless content matches data to be removed.
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals(lineToRemove)) {
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();
			
			//Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}
			//Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * changes an item record
	 * @param file
	 * @param row
	 * @param lineToChange
	 */
	public void changeLineFromFile(String file, int row, String lineToChange) {
		try {
			File inFile = new File(file);
			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}
			//Construct the new file that will later be renamed to the original filename. 
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			String line ;
			//Read from the original file and write to the new 
			//unless content matches data to be removed.
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i == row) {
					line = lineToChange;
				}
				pw.println(line);
				pw.flush();
				i++;
			}
			pw.close();
			br.close();
			
			//Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}
			//Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
