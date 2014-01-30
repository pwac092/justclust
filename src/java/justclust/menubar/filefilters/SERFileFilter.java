package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SERFileFilter extends FileFilter
{

	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".ser");
	}

	public String getDescription()
	{
		return "SER (SERialized object) (*.ser)";
	}

}
