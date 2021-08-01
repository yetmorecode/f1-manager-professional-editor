package yetmorecode.f1mp.ui.tools.tracks;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import yetmorecode.f1mp.model.track.Track;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;
import yetmorecode.file.BinaryFileInputStream;

public class TracksToolPanel extends AbstractFileToolPanel {
	
	private static final long serialVersionUID = 560569333849827163L;

	private ArrayList<Track> tracks;
	
	public TracksToolPanel(String filename) {
		super(filename, filename);
		
		tracks = new ArrayList<>();
		try {
			var input = new BinaryFileInputStream(filename);
			
			long i = 0;
			while (i < input.getChannel().size()) {
				var t = Track.createFrom(input, i);
				tracks.add(t);
				
				var node = new DefaultMutableTreeNode(t);
				top.add(node);
				
				map.addKnownRegion(new FileMapRegion(t.fileOffset, t.fileOffset + t.fileSize));
				i += 0x26350;
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
		if (data instanceof Track) {
			Track t = (Track)data;
			map.highlightRegion(t.fileOffset);
			setToolContent(new TrackPanel(t));
		} else {
			map.highlightRegion(-1);
		}
	}
}
