package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This class has instances which act as file filters for the CSV file format.
 */
public class CSVFileFilter extends FileFilter
{

	/**
	 * This method returns true when the file contained in a parameter has the
	 * CSV file format.
	 */
	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".csv");
	}

	/**
	 * This method returns a description of the CSV file format.
	 */
	public String getDescription()
	{
		return "CSV (Comma-Separated Values) (*.csv)";
	}

}
