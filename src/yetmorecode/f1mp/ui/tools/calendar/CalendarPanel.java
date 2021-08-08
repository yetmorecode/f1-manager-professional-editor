package yetmorecode.f1mp.ui.tools.calendar;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import yetmorecode.f1mp.model.F1Model;

public class CalendarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856521264768162100L;
	
	public F1Model model; 
	
	public CalendarPanel(F1Model model) {
		this.model = model;
		
		String[] columnNames = {
			"Country",
			"Region",
			"Hour",
            "Day",
            "Month",
            "Year",
            "Distance",
            "Laps",
            "Lap length",
            "Feature1",
            "Feature2",
            "Feature3",
            "Feature4",
            "unknown"
        };
		final DefaultTableModel m = new DefaultTableModel( columnNames, 0 );
		
		for (var e : model.calendar.entrySet()) {
			Vector<String> v = new Vector<>(m.getColumnCount());
			var t = e.getValue();
			v.add(t.country);
			v.add(String.format("%d", t.region));
			v.add(String.format("%d", t.date.hour));
			v.add(String.format("%d", t.date.day));
			v.add(String.format("%d", t.date.month));
			v.add(String.format("%d", t.date.year));
			v.add(String.format("%d", t.raceLength));
			v.add(String.format("%d", t.laps));
			v.add(String.format("%d", t.lapLength));
			v.add(t.feature1);
			v.add(t.feature2);
			v.add(t.feature3);
			v.add(t.feature4);
			v.add(String.format("%d %d %d %d %d %d %d", t.unknown1, t.unknown2, t.unknown3, t.unknown4, t.unknown5, t.unknown6, t.unknown7));
			m.addRow(v);
		}
		
		
		JTable table = new JTable(m);
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		setLayout(new BorderLayout());
		add(scrollPane);
	}
	
	

}
