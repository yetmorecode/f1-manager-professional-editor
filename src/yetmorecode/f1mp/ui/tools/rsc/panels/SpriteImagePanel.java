package yetmorecode.f1mp.ui.tools.rsc.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;

import yetmorecode.f1mp.model.rsc.SpriteResource;

public class SpriteImagePanel extends JPanel {
	private static final long serialVersionUID = 4470777298821205654L;
	private static final int PIXEL_SIZE = 1;
	private static final int SPACING = 10;
	
	public SpriteResource sprite;
	
	public SpriteImagePanel(SpriteResource sprite) throws IOException {
		this.sprite = sprite;
		System.out.println(String.format("%d items, %d - %d", sprite.totalItems, sprite.width, sprite.height));
		setPreferredSize(new Dimension(
			sprite.width * PIXEL_SIZE + 10, 
			(sprite.height + SPACING) * PIXEL_SIZE * sprite.totalItems
		));
	}
	
	public void paintComponent(Graphics graphics) {
	    super.paintComponent(graphics);
	    Graphics2D g2d = (Graphics2D) graphics;

	    var spacing = 0;
		for (int i=0; i < sprite.totalItems; i++, spacing += SPACING) {
			var pixels = sprite.items.get(i);
			
			for (int h = 0; h < sprite.height; h++) {
				for (int w = 0; w < sprite.width; w++) {
		    		var c = pixels.get(h*(sprite.width)+w);
		    		g2d.setColor(new Color(c.r, c.g, c.b));
		    		g2d.fillRect(10 + w*PIXEL_SIZE, (i * sprite.height + h) * PIXEL_SIZE + spacing, PIXEL_SIZE, PIXEL_SIZE);	
		    	}	
			}
	    	
	    } 
	}
}
