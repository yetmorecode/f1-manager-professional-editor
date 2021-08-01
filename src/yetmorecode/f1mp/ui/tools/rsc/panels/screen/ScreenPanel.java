package yetmorecode.f1mp.ui.tools.rsc.panels.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.rsc.PCXResource;
import yetmorecode.f1mp.model.rsc.SpriteResource;
import yetmorecode.file.format.pcx.VGAPalette;

public class ScreenPanel extends JPanel {

	private static final long serialVersionUID = 1428070017531188946L;

	public int pixelSize = 1;
	public int width = 640;
	public int height = 480;
	public ArrayList<ScreenItem> pcxItems = new ArrayList<>();
	public ArrayList<ScreenItem> spriteItems = new ArrayList<>();
	public VGAPalette palette;
	
	public ScreenPanel(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
	}
	
	public ScreenPanel(int width, int height, int pixelSize) {
		this(width, height);
		this.pixelSize = pixelSize;
	}
	
	public void clear() {
		pcxItems = new ArrayList<>();
		spriteItems = new ArrayList<>();
	}
	
	public void addPCX(PCXResource pcx, int x, int y) {
		var i = new ScreenItem();
		i.x = x;
		i.y = y;
		i.item = pcx;
		pcxItems.add(i);
	}

	public void addSprite(SpriteResource sprite, int x, int y, int index) {
		var i = new ScreenItem();
		i.x = x;
		i.y = y;
		i.index = index;
		i.item = sprite;
		spriteItems.add(i);
	}	

	public void addSprite(SpriteResource sprite, int x, int y) {
		var i = new ScreenItem();
		i.x = x;
		i.y = y;
		i.item = sprite;
		spriteItems.add(i);
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;
		for (ScreenItem i : pcxItems) {
			if (i.item instanceof PCXResource) {
				PCXResource pcx = (PCXResource) i.item;
				paintPCX(g2d, pcx, i.x, i.y);
			}
		}
		
		for (ScreenItem i : spriteItems) {
			if (i.item instanceof SpriteResource) {
				SpriteResource sprite = (SpriteResource) i.item;
				paintSprite(g2d, sprite, i.x, i.y, i.index);
			}
		}
	}
	
	public void paintPCX(Graphics2D g2d, PCXResource pcx, int x, int y) {
		for (int i=0; i < pcx.getHeight(); i++) {
	    	for (int j = 0; j < pcx.getWidth(); j++) {
	    		if (x + j < pcx.getWidth() && y + i < pcx.getHeight()) {
	    			var c = pcx.pixels.get(i * pcx.getWidth() + j);
		    		try {
		    			g2d.setColor(new Color(c.r, c.g, c.b));
		    		} catch (IllegalArgumentException e) {
		    			System.out.println(String.format("%d %d %d", c.r, c.g, c.b));
		    		}
	    			g2d.fillRect((x + j) * pixelSize, (y + i) * pixelSize, pixelSize, pixelSize);	
	    		}
	    	}
	    } 
	}
	
	public void paintSprite(Graphics2D g2d, SpriteResource sprite, int x, int y, int i) {
		var pixels = sprite.items.get(i);
		
		for (int h = 0; h < sprite.height; h++) {
			for (int w = 0; w < sprite.width; w++) {
	    		var c = pixels.get(h*(sprite.width)+w);
	    		if (!c.transparent) {
	    			g2d.setColor(new Color(c.r, c.g, c.b));
		    		g2d.fillRect((x + w) * pixelSize, (y + h) * pixelSize, pixelSize, pixelSize);	
	    		}
	    	}	
		}
	}
}
