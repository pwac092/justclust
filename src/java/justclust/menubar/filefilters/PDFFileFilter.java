package justclust.menubar.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class PDFFileFilter extends FileFilter
{

	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			return true;
		}
		return file.getName().endsWith(".pdf");
	}

	public String getDescription()
	{
		return "PDF (Portable Document Format) (*.pdf)";
	}

}
