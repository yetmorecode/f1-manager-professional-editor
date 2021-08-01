package yetmorecode.f1mp.ui.tools.engines;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import yetmorecode.f1mp.model.engine.Engine;
import yetmorecode.f1mp.model.engine.EngineResources;
import yetmorecode.f1mp.model.rsc.PaletteResource;
import yetmorecode.f1mp.model.rsc.SpriteResource;
import yetmorecode.f1mp.ui.components.SimpleDocumentListener;
import yetmorecode.f1mp.ui.tools.rsc.panels.SpriteImagePanel;
import yetmorecode.f1mp.ui.tools.rsc.panels.screen.ScreenPanel;
import yetmorecode.file.BinaryFileInputStream;

public class EnginePanel extends JPanel {

	private static final long serialVersionUID = -6137227013287803107L;
	
	private Engine engine;
	private JPanel info;
	
	private JLabel modelLabel = new JLabel("Model:");
	private JLabel manufacturerLabel = new JLabel("Manufacturer:");
	private JLabel costLabel = new JLabel("Cost [$]:");
	private JTextField modelName = new JTextField();
	private JTextField manufacturerName = new JTextField();
	private JTextField stars = new JTextField();
	private JTextField logoNumber = new JTextField();
	private JTextField cost = new JTextField();
	private JTextField width = new JTextField();
	private JTextField height = new JTextField();
	private JTextField length = new JTextField();
	private JTextField weight = new JTextField();
	private JTextField rpm = new JTextField();
	private JTextField hp = new JTextField();
	private JTextField cc = new JTextField();
	private JTextField cylinders = new JTextField();
	private JTextField valves = new JTextField();
	private JTextField cooling = new JTextField();
	
	private SpriteResource logos;
	private ScreenPanel screen;
	
	public EnginePanel(Engine e, File rscFile) {
		this.engine = e;
		
		//var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		var layout = new BorderLayout();
		setLayout(layout);
	
		info = new JPanel();
		info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		var grid = new GridLayout(0, 3);
		grid.setVgap(4);
		info.setLayout(grid);
		
		
		PaletteResource palette;
		try {
			var input = new BinaryFileInputStream(rscFile);
			palette = PaletteResource.createFrom(input, 0x9c768f);
			//var sprite = SpriteResource.createFrom(input, 0x17c96db, palette.colors);
			logos = SpriteResource.createFrom(input, EngineResources.PCX_ENGINE_SUPPLIER_LOGOS, palette.colors);
			
			
			screen = new ScreenPanel(100, 100);
			screen.palette = palette;
			add(screen, BorderLayout.EAST);
			updateLogo();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		manufacturerName.setText(engine.shortname);
		manufacturerName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				var f = manufacturerLabel.getFont();
				manufacturerLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
			}
			@Override
			public void focusLost(FocusEvent e) {
				engine.shortname = manufacturerName.getText();
				var f = manufacturerLabel.getFont();
				manufacturerLabel.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
			}
		});
		addRow(manufacturerLabel, manufacturerName);
		
		modelName.setText(engine.name);
		modelName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				var f = modelLabel.getFont();
				modelLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
			}
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("blur name");
				engine.name = modelName.getText();
				var f = modelLabel.getFont();
				modelLabel.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
			}
		});
		addRow(modelLabel, modelName);
		
		cost.setText(String.format("%d", engine.cost));
		cost.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.cost = Integer.parseInt(cost.getText(), 10);
			}
		});
		addRow(costLabel, cost);
		
		logoNumber.setText(String.format("%d", EngineResources.engineToLogo.get(engine.index)));
		logoNumber.getDocument().addDocumentListener((SimpleDocumentListener) event -> {
			if (logoNumber.getText().length() > 0) {
				try {
					var x = Integer.parseInt(logoNumber.getText(), 10);
					if (x >= 1 && x <= 8) {
						EngineResources.engineToLogo.put(engine.index, x);
						engine.dirty = true;
						updateLogo();
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}		
			}
		});
		addRow("Logo Nummer [1-8]:", logoNumber);
		
		stars.setText(String.format("%d", engine.stars));
		stars.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.stars = Integer.parseInt(stars.getText(), 10);
			}
		});
		addRow("Stars [1-5]:", stars);
		
		hp.setText(String.format("%d", engine.hp));
		hp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.hp = Integer.parseInt(hp.getText(), 10);
			}
		});
		addRow("HP:", hp);
		
		rpm.setText(engine.rpm);
		rpm.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.rpm = rpm.getText();
			}
		});
		addRow("RPM:", rpm);
		
		cc.setText(engine.cubicCapacity);
		cc.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.cubicCapacity = cc.getText();
			}
		});
		addRow("Cubic capacity:", cc);
		
		cylinders.setText(engine.cylinders);
		cylinders.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.cylinders = cylinders.getText();
			}
		});
		addRow("Cylinders:", cylinders);
		
		valves.setText(engine.valves);
		valves.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.valves = valves.getText();
			}
		});
		addRow("Valves:", valves);
		
		height.setText(String.format("%d", engine.height));
		height.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.height = Integer.parseInt(height.getText(), 10);
			}
		});
		addRow("Height [mm]:", height);
		
		width.setText(String.format("%d", engine.width));
		width.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.width = Integer.parseInt(width.getText(), 10);
			}
		});
		addRow("Width [mm]:", width);
		
		length.setText(String.format("%d", engine.length));
		length.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.length = Integer.parseInt(length.getText(), 10);
			}
		});
		addRow("Length [mm]:", length);
		
		weight.setText(String.format("%d", engine.weight));
		weight.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.weight = Integer.parseInt(weight.getText(), 10);
			}
		});
		addRow("Weight [g]:", weight);
		
		cooling.setText(String.format("%d", engine.cooling));
		cooling.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				engine.cooling = Integer.parseInt(cooling.getText(), 10);
			}
		});
		addRow("Cooling [0-100]:", cooling);
		
		info.setBorder(new EmptyBorder(10, 0, 20, 0));
		add(info);
	}
	
	private void updateLogo() {
		screen.clear();
		screen.addSprite(logos, 0, 0, EngineResources.engineToLogo.get(engine.index)-1);
		screen.validate();
		screen.repaint();
	}
	
	private void addRow(JLabel label, JComponent input) {
		info.add(label);
		info.add(input);
		info.add(new JLabel(""));
	}
	
	private void addRow(String label, JComponent input) {
		var l = new JLabel(label);
		info.add(l);
		info.add(input);
		info.add(new JLabel(""));
	}
}
