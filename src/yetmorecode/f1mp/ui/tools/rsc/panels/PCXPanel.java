package yetmorecode.f1mp.ui.tools.rsc.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import yetmorecode.f1mp.model.rsc.PCXResource;
import yetmorecode.file.BinaryFileInputStream;

public class PCXPanel extends JPanel {
	private static final long serialVersionUID = -8619504547811840105L;
	
	public PCXImagePanel image;
	JPanel info;
	private PCXResource pcx;
	
	
	public PCXPanel(String file, long offset) throws IOException {
		var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		var input = new BinaryFileInputStream(file);
		var old = input.position(offset);
    	pcx = PCXResource.createFrom(input, offset);
    	if (pcx.manufacturer != 0xa || pcx.version != 0x5) {
    		throw new RuntimeException("Not a PCX resource");
    	} else {
    		System.out.println(String.format("PCX: %x%x %d %d x %d", pcx.manufacturer, pcx.version, pcx.fileSize, pcx.getWidth(), pcx.getHeight()));
    	}
		input.position(old);
		
		image = new PCXImagePanel(file, pcx);
	
		info = new JPanel();
		info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		info.setLayout(new GridLayout(0, 6));
		var label = new JLabel("File offset: ");
		info.add(label);
		info.add(new JLabel("0x" + Long.toString(offset, 16) + " - 0x" + Long.toString(offset + pcx.fileSize, 16)));
		info.add(new JLabel(""));
		var btn = new JButton("Export PCX");
		btn.addActionListener((event) -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Specify a file to save");
			
			var root = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
			fileChooser.setSelectedFile(new File(root + "\\f1e_" + String.format("%x",  offset) + ".pcx"));
			 
			int userSelection = fileChooser.showSaveDialog(PCXPanel.this);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave = fileChooser.getSelectedFile();
			    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			    
			    try {
					FileOutputStream out = new FileOutputStream(fileToSave);
					BinaryFileInputStream in = new BinaryFileInputStream(file);
					in.position(offset);
					int s = in.readInt();
					out.write(in.readNBytes(s));
					out.close();
					in.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
		});
		info.add(btn);
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		
		info.add(new JLabel("Image dimensions:"));
		info.add(new JLabel(Long.toString(pcx.getWidth()) + " x " + Long.toString(pcx.getHeight()) + " pixel"));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		
		
		info.add(new JLabel("Compressed size:"));
		info.add(new JLabel("0x" + Long.toString(pcx.fileSize, 16) + " (" + Long.toString(pcx.fileSize / 1024) + " kb)"));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		info.add(new JLabel(""));
		
		info.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		add(info);
		add(image);
	}
}
