package yetmorecode.f1mp.ui.tools.tracks;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import yetmorecode.f1mp.model.track.Track;

public class TrackPanel extends JPanel {

	private static final long serialVersionUID = 5355739396749325089L;
	
	JPanel info;
	Track track;
	TrackImagePanel trackImage;
	JLabel zAxisLabel;
	String zAxisName;
	String gameDirectory;
	
	public TrackPanel(Track track) {
		this.track = track;
		
		var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
	
		info = new JPanel();
		info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		info.setLayout(new GridLayout(0, 3));
		var label = new JLabel("Track name/number: ");
		info.add(label);
		info.add(new JLabel(String.format("%s (%d) / %s (%d)", track.name, track.number, track.getRegionName(), track.region)));
		var btn = new JButton("Export PNG");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				
				var root = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
				fileChooser.setSelectedFile(new File(root + "\\" + track.name.trim() + "-" + zAxisName + ".png"));
				 
				int userSelection = fileChooser.showSaveDialog(TrackPanel.this);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				    
				    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
				    Graphics2D g = image.createGraphics();
				    printAll(g);
				    g.dispose();
				    try { 
				        ImageIO.write(image, "png", fileToSave); 
				    } catch (IOException ex) {
				        ex.printStackTrace();
				    }
				}		
			}
		});
		info.add(btn);
		
		info.add(new JLabel("Segments/Length:"));
		info.add(new JLabel(String.format("%dm", track.segmentsTotal)));
		info.add(new JLabel(""));
		
		info.add(new JLabel("Camera name:"));
		info.add(new JLabel(track.cameraName));
		info.add(new JLabel(""));
		
		info.add(new JLabel("Pit entry/exit:"));
		info.add(new JLabel(String.format("%d - %d (%dm)", track.pitEntry, track.pitExit, (track.pitExit - track.pitEntry + track.segmentsTotal) % track.segmentsTotal)));
		info.add(new JLabel(""));
		
		info.add(new JLabel("Pit limiter entry/exit:"));
		info.add(new JLabel(String.format("%d - %d (%dm)", track.pitLimiterEntry, track.pitLimiterExit, track.pitLimiterExit - track.pitLimiterEntry)));
		info.add(new JLabel(""));
		
		var sj = new StringJoiner(", ");
		for (var i : track.garages) {
			sj.add(i.toString());
		}
		info.add(new JLabel("Garages:"));
		info.add(new JLabel(String.format("%s", sj.toString())));
		info.add(new JLabel(""));
		
		info.add(new JLabel("Sprites (f1_e.rsc):"));
		info.add(new JLabel(String.format("0x%x, 0x%x, 0x%x", track.sprite1, track.sprite2, track.spriteStandings)));
		info.add(new JLabel(""));
		
		
		info.add(new JLabel("Fuel / Weather / Index:"));
		info.add(new JLabel(String.format("%d / %d / %d", track.fuel, track.weather, track.index)));
		info.add(new JLabel(""));
				
		var height = new JRadioButton("height", true);
		height.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(trackImage);
				trackImage = new TrackImagePanel(track, s -> s.z);
				zAxisName = "height";
				updateZAxisLabel();
				add(trackImage);
				validate();
				repaint();
			}
		});
		var a = new JRadioButton("racing line");
		a.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(trackImage);
				trackImage = new TrackImagePanel(track, s -> s.b - s.a / 2);
				zAxisName = "racing-line";
				updateZAxisLabel();
				add(trackImage);	
				validate();
				repaint();
			}
		});

		var group = new ButtonGroup();
		group.add(height);
		group.add(a);
		
		trackImage = new TrackImagePanel(track, s -> s.z);
		
		info.add(new JLabel("Graph data:"));
		var options = new JPanel();
		options.setLayout(new GridLayout(0, 3));
		options.add(height);
		options.add(a);
		info.add(options);
		zAxisLabel = new JLabel();
		zAxisLabel.setAlignmentX(RIGHT_ALIGNMENT);
		zAxisName = "height";
		updateZAxisLabel();
		info.add(zAxisLabel);
		
		info.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		add(info);
		
		
		add(trackImage);
	}
	
	private void updateZAxisLabel() {
		zAxisLabel.setText(String.format("[%f, %f]", trackImage.zMin, trackImage.zMax));
	}

}
