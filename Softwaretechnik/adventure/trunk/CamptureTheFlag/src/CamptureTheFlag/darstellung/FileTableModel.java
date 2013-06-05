package CamptureTheFlag.darstellung;

import java.io.File;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import CamptureTheFlag.logik.inits.GameRepository;

/** 
 * TabellenModel, das die Inhalte eines Verzeichnis einliest.
 * @author dgens
 */
public class FileTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3561940044826812986L;

	String titles[] = new String[] {
	    "Directory?", "File Name", "Read?", "Write?", "Size", "Last Modified"
	  };

	  Class<?> types[] = new Class[] { 
	    Boolean.class, String.class, Boolean.class, Boolean.class,
	    Number.class, Date.class
	  };
	      
	  Object data[][];

	  public FileTableModel() { this("."); }

	  public FileTableModel(String dir) {
	    File pwd = new File(dir);
	    setFileStats(pwd);
	  }

	  public int getRowCount() { return data.length; }
	  public int getColumnCount() { return titles.length; }
	  public String getColumnName(int c) { return titles[c]; }
	  public Class<?> getColumnClass(int c) { return types[c]; }
	  public Object getValueAt(int r, int c) { return data[r][c]; }

	  public void setFileStats(File dir) {
	    String files[] = dir.list();
	    if (files == null)
	    	throw new RuntimeException("Spiel laden: Den Ordner " + dir.getAbsolutePath() + " gibt es nicht.");
	    
	    int noElem = 0;
	    for (int i=0; i < files.length; i++) {
		      if (files[i].endsWith(GameRepository.FILE_EXT))
			      ++noElem;
		}
	    
	    data = new Object[noElem][6];
	    noElem = 0;

	    for (int i=0; i < files.length; i++) {
	      File tmp = new File(files[i]);
	      if (tmp.getPath().endsWith(GameRepository.FILE_EXT)) {
		      data[noElem][0] = new Boolean(tmp.isDirectory());
		      data[noElem][1] = tmp.getName();
		      data[noElem][2] = new Boolean(tmp.canRead());
		      data[noElem][3] = new Boolean(tmp.canWrite());
		      data[noElem][4] = new Long(tmp.length());
		      data[noElem][5] = new Date(tmp.lastModified());
		      ++noElem;
	      }
	    }

	    fireTableDataChanged();
	  }
}
