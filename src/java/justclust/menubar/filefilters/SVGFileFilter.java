package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SVGFileFilter extends FileFilter
{

	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".svg");
	}

	public String getDescription()
	{
		return "SVG (Scalable Vector Graphics) (*.svg)";
	}

}
