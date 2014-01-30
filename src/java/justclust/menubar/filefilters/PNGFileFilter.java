package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This class has instances which act as file filters for the PNG file format.
 */
public class PNGFileFilter extends FileFilter
{

	/**
	 * This method returns true when the file contained in a parameter has the
	 * PNG file format.
	 */
	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".png");
	}

	/**
	 * This method returns a description of the PNG file format.
	 */
	public String getDescription()
	{
		return "PNG (Portable Network Graphics) (*.png)";
	}

}
