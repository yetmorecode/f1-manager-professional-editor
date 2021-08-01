package yetmorecode.f1mp.ui.components.filemap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FileMapPanel extends JPanel {

	private static final long serialVersionUID = -4468688136922120006L;
	private long start;
	private long end;
	
	private long highlightOffset = -1;
	
	private ArrayList<FileMapRegion> knownRegions;
	
	
	public FileMapPanel(String filename) {
		knownRegions = new ArrayList<>();
		
		var f = new File(filename);
		start = 0;
		end = f.length();
	}
	
	public void highlightRegion(long offset) {
		highlightOffset = offset;
		repaint();
	}
	
	public void addKnownRegion(FileMapRegion region) {
		knownRegions.add(region);
		repaint();
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

	    Dimension size = getSize();
	    var width = size.width;
	    
	    FileMapRegion highlightRegion = null;
	    for (FileMapRegion r : knownRegions) {
	    	var bgColor = Color.DARK_GRAY;
	    	var fgColor = Color.GRAY;
    		if (highlightOffset >= r.start && highlightOffset < r.end) {
    			highlightRegion = r;
    			bgColor = Color.GRAY;
    			fgColor = Color.CYAN;
    		}
	    	
	    	int xBegin = (int) (1.0 * r.start / (end - start) * width);
	    	int xEnd = (int) (1.0 * r.end / (end - start) * width);
	    	g2d.setColor(bgColor);
    		g2d.fillRect(xBegin, 0, xEnd - xBegin, size.height - 1);
    		
    		float thickness = 1;
    		Stroke oldStroke = g2d.getStroke();
    		g2d.setColor(fgColor);
    		g2d.setStroke(new BasicStroke(thickness));
    		g2d.drawRect(xBegin, 0, xEnd - xBegin, size.height - 1);
    		g2d.setStroke(oldStroke);
	    }
	    
	    if (highlightRegion != null) {
	    	var bgColor = Color.GRAY;
			var fgColor = Color.CYAN;
			var r = highlightRegion;
	    	int xBegin = (int) (1.0 * r.start / (end - start) * width);
	    	int xEnd = (int) (1.0 * r.end / (end - start) * width);
	    	g2d.setColor(bgColor);
    		g2d.fillRect(xBegin, 0, xEnd - xBegin, size.height - 1);
    		
    		float thickness = 1;
    		Stroke oldStroke = g2d.getStroke();
    		g2d.setColor(fgColor);
    		g2d.setStroke(new BasicStroke(thickness));
    		g2d.drawRect(xBegin, 0, xEnd - xBegin, size.height - 1);
    		g2d.setStroke(oldStroke);
	    }
	    
	    setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));
	}
}
