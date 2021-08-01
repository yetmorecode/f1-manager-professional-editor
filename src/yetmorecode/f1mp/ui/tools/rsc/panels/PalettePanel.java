package yetmorecode.f1mp.ui.tools.rsc.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.rsc.PaletteResource;
import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.pcx.RGBColor;

public class PalettePanel extends JPanel {
	private static final long serialVersionUID = 8218483032643388806L;

	public final static int COLOR_SIZE = 20;
	
	public PaletteResource palette;
	
	public PalettePanel(String filename, long offset) throws IOException {
		var input = new BinaryFileInputStream(filename);
		palette = PaletteResource.createFrom(input, offset);	
		setPreferredSize(new Dimension(40*COLOR_SIZE, 256*COLOR_SIZE));
		input.close();
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

    	// Should be size of palette 0x300, but don't bother
		for (int i=0; i < 16; i++) {
	    	for (int j = 0; j < 16; j++) {
	    		int index = i*16+j;
	    		var c = palette.colors.get(index);
	    		g2d.setColor(new Color(c.r, c.g, c.b));
	    		g2d.fillRect(0, index*COLOR_SIZE, COLOR_SIZE*30, COLOR_SIZE);
	    		g2d.drawString(String.format("%d #%02x%02x%02x", index, c.r, c.g, c.b), COLOR_SIZE*32, index*COLOR_SIZE+14);
	    	}
	    }   
	}
}
