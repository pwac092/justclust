package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JPGFileFilter extends FileFilter
{

	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".jpg");
	}

	public String getDescription()
	{
		return "JPG (Joint Photographic expert Group) (*.jpg)";
	}

}
