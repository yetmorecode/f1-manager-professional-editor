package yetmorecode.f1mp.ui.tools.points;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PointsToolPanel extends JPanel {

	public long OFFSET_SCORERS = 0xdfcca + 3;
	public long OFFSET_POINTS = 0x1f9f6c;
	public long OFFSET_MEMREAD1 = 0xdfd27;
	public long OFFSET_MEMREAD2 = 0xdfd53;
	public long OFFSET_MEMREAD3 = 0xdfdca;
	public long OFFSET_MEMREAD4 = 0xdfe00;
	public long OFFSET_MEMREAD5 = 0xdfd94;
	
	boolean useNewPoints = false;
	String filename;
	
	JMenuBar menubar;
	JMenu menu;
	JMenuItem menuOpen;
	GridLayout layout;
	
	JLabel labelFile;
	JLabel typeLabel;
	JCheckBox checkboxUseNewPoints;
	JTextField pointFields[];
	
	JButton btnUpdate;
	JButton btnUpdateModern;
	JButton btnRestore;
	JLabel labelStatus;
	JLabel labelStatus2;
	
	Preferences prefs;
	
	public PointsToolPanel(String file) throws HeadlessException {
		super();
		
	    layout = new GridLayout(0, 2);
	    this.setLayout(layout);
	    add(new JLabel("File:"));
	    labelFile = new JLabel();
	    add(labelFile);
	    add(new JLabel("Type:"));
	    typeLabel = new JLabel();
	    add(typeLabel);
	    add(new JLabel("Use new points:"));
	    checkboxUseNewPoints = new JCheckBox();
	    checkboxUseNewPoints.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setFile(filename);
				
			}
		});
	    add(checkboxUseNewPoints);
	    
	    pointFields = new JTextField[24];
	    for (int i = 0; i < 24; i++) {
	    	pointFields[i] = new JTextField();
	    
	    	add(new JLabel("" + (i+1) + "."));
	    	add(pointFields[i]);
	    }
	    
	    
	    btnUpdate = new JButton("Update custom");
	    btnUpdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				writeChanges();
			}
		});
	    btnRestore = new JButton("Restore 10-6-4-3-2-1");
	    btnRestore.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkboxUseNewPoints.setSelected(false);
				pointFields[0].setText("10");
				pointFields[1].setText("6");
				pointFields[2].setText("4");
				pointFields[3].setText("3");
				pointFields[4].setText("2");
				pointFields[5].setText("1");
				for (int i = 6; i < 24; i++) {
					pointFields[i].setText("0");
				}
				writeChanges();
			}
		});
	    labelStatus = new JLabel();
	    labelStatus2 = new JLabel();
	    add(labelStatus);
	    add(btnRestore);
	    add(labelStatus2);
	    btnUpdateModern = new JButton("Update 25-18-15-12-10-8-6-4-2-1");
	    btnUpdateModern.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkboxUseNewPoints.setSelected(true);
				pointFields[0].setText("25");
				pointFields[1].setText("18");
				pointFields[2].setText("15");
				pointFields[3].setText("12");
				pointFields[4].setText("10");
				pointFields[5].setText("8");
				pointFields[6].setText("6");
				pointFields[7].setText("4");
				pointFields[8].setText("2");
				pointFields[9].setText("1");
				for (int i = 10; i < 24; i++) {
					pointFields[i].setText("0");
				}
				writeChanges();
			}
		});
	    add(btnUpdateModern);
	    add(new JLabel());
	    add(btnUpdate);
	    
	    
	    setFile(file);
	}
	
	public void setFile(String filename) {
		this.filename = filename;
		labelStatus.setText("Open success");
		labelFile.setText(filename);
		try {
			File f = new File(filename);
			if (!f.exists()) {
				return;
			}
			FileInputStream input = new FileInputStream(filename);
			input.getChannel().position(OFFSET_SCORERS);
			int scorers = input.read();
			
			input.getChannel().position(OFFSET_MEMREAD1);
			//int memoryRead1 = input.read();
			input.getChannel().position(OFFSET_MEMREAD2);
			//int memoryRead2 = input.read();
			
			//boolean is4Byte = memoryRead1 == 0x8b && memoryRead2 == 0x8b;
			if (!checkboxUseNewPoints.isSelected()) {
				typeLabel.setText("6 scorers a 4 bytes");
				input.getChannel().position(OFFSET_POINTS);
				for (int i = 0; i <= scorers; i++) {
					pointFields[i].setText("" + input.read());
					input.read();
					input.read();
					input.read();
				}
				labelStatus2.setText("unmodded");
			} else {
				scorers = 23;
				typeLabel.setText("" + (scorers+1) + " scorers a 1 byte");
				input.getChannel().position(OFFSET_POINTS);
				for (int i = 0; i <= scorers; i++) {
					pointFields[i].setText("" + input.read());
				}
				//checkboxUseNewPoints.setSelected(true);
			}
			input.close();
			
			updateFields();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void writeChanges() {
		RandomAccessFile f;
		try {
			f = new RandomAccessFile(filename, "rw");
			
			if (!checkboxUseNewPoints.isSelected()) {
				f.seek(OFFSET_SCORERS);
				f.writeByte(5);
				f.seek(0x1f9f6c);
				for (int i = 0; i < 6; i++) {
					f.writeByte(Integer.parseInt(pointFields[i].getText()));
					f.writeByte(0);
					f.writeByte(0);
					f.writeByte(0);
				}
				f.seek(OFFSET_MEMREAD1-3);
				f.writeByte(0xc1);
				f.writeByte(0xe0);
				f.writeByte(0x02);
				f.writeByte(0x8b);
				f.seek(OFFSET_MEMREAD2-3);
				f.writeByte(0xc1);
				f.writeByte(0xe0);
				f.writeByte(0x02);
				f.writeByte(0x8b);
				f.seek(OFFSET_MEMREAD3-3);
				f.writeByte(0xc1);
				f.writeByte(0xe0);
				f.writeByte(0x02);
				f.writeByte(0x8b);
				f.seek(OFFSET_MEMREAD4-3);
				f.writeByte(0xc1);
				f.writeByte(0xe0);
				f.writeByte(0x02);
				f.writeByte(0x8b);
				f.seek(OFFSET_MEMREAD5-3);
				f.writeByte(0xc1);
				f.writeByte(0xe0);
				f.writeByte(0x02);
				f.writeByte(0x8b);
			} else {
				f.seek(OFFSET_SCORERS);
				f.writeByte(23);
				f.seek(0x1f9f6c);
				for (int i = 0; i < 24; i++) {
					f.writeByte(Integer.parseInt(pointFields[i].getText()));
				}
				f.seek(OFFSET_MEMREAD1-3);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x8a);
				f.seek(OFFSET_MEMREAD2-3);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x8a);
				f.seek(OFFSET_MEMREAD3-3);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x8a);
				f.seek(OFFSET_MEMREAD4-3);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x8a);
				f.seek(OFFSET_MEMREAD5-3);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x90);
				f.writeByte(0x8a);
			}
			f.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
	
	protected void updateFields() {
		if (checkboxUseNewPoints.isSelected()) {
			for (int i = 0; i < 24; i++) {
				pointFields[i].setEnabled(true);
			}
		} else {
			for (int i = 0; i < 24; i++) {
				pointFields[i].setEnabled(i < 6);
				if (i >= 6) {
					pointFields[i].setText("0");
				}
			}
		}
	}
	
	private static final long serialVersionUID = 1427412241306169766L;

}
