package yetmorecode.f1mp.ui.tools.history;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.history.SeasonHistory;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;
import yetmorecode.file.BinaryFileInputStream;

public class HistoryToolPanel extends AbstractFileToolPanel {

	private static final long serialVersionUID = 3024160335351899286L;

	private ArrayList<SeasonHistory> seasons;
	
	public HistoryToolPanel(String filename) {
		super(filename, filename);
		
		seasons = new ArrayList<>();
		try {
			var input = new BinaryFileInputStream(filename);
			
			long offset = 0;
			while (offset < input.getChannel().size()) {
				var t = SeasonHistory.createFrom(input, offset);
				seasons.add(t);
				
				var node = new DefaultMutableTreeNode(t);
				top.add(node);
				
				var drivers = new DefaultMutableTreeNode(String.format("Drivers (%d)", t.numDrivers));
				var teams = new DefaultMutableTreeNode(String.format("Teams (%d)", t.numTeams));
				var tracks = new DefaultMutableTreeNode(String.format("Races (%d)", t.numDrivers));
				node.add(drivers);
				for (int i = 0; i < t.drivers.size(); i++) {
					var n = new DefaultMutableTreeNode(t.drivers.get(i));
					drivers.add(n);
				}
				node.add(teams);
				node.add(tracks);
				for (int i = 0; i < t.tracks.size(); i++) {
					var n = new DefaultMutableTreeNode(t.tracks.get(i));
					tracks.add(n);
				}
				
				map.addKnownRegion(new FileMapRegion(offset, offset + SeasonHistory.SIZE));
				offset += SeasonHistory.SIZE;
			}
			treeModel.reload();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof SeasonHistory) {
			var h = (SeasonHistory)data;
			map.highlightRegion(h.fileOffset);
			//setToolContent(new TrackPanel(t));
		} else {
			map.highlightRegion(-1);
		}
	}

}
