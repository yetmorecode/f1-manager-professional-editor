package yetmorecode.f1mp.ui.tools.rsc.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import yetmorecode.f1mp.model.rsc.SpriteResource;
import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.pcx.RGBColor;

public class SpritePanel extends JPanel {

	private static final long serialVersionUID = -6108924909268666462L;

	public SpriteResource sprite;
	private JPanel info;
	private SpriteImagePanel image;
	
	
	public SpritePanel(String file, long offset, ArrayList<RGBColor> palette) {
		var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		BinaryFileInputStream input;
		try {
			input = new BinaryFileInputStream(file);
			var old = input.position(offset);
	    	sprite = SpriteResource.createFrom(input, offset, palette);
			input.position(old);
			image = new SpriteImagePanel(sprite);
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		info = new JPanel();
		info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		info.setLayout(new GridLayout(0, 6));
		
		info.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		add(info);
		add(image);
	}
}
