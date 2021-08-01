package yetmorecode.f1mp.ui.tools.teams;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.team.Team;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;

public class TeamsToolPanel extends AbstractFileToolPanel {

	private static final long serialVersionUID = -4412301086031074196L;

	public TeamsToolPanel(File file, ArrayList<Team> teams) {
		super(file.getAbsolutePath(), file.getAbsolutePath());
		for (int i = 0; i < teams.size(); i++) {
			var t = teams.get(i);
			
			var node = new DefaultMutableTreeNode(t);
			top.add(node);
			
			map.addKnownRegion(new FileMapRegion(t.fileOffset, t.fileOffset + Team.SIZE));
		}
		treeModel.reload();
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof Team) {
			var h = (Team)data;
			map.highlightRegion(h.fileOffset);
			//setToolContent(new Spons(h));
		} else {
			map.highlightRegion(-1);
		}
	}

}
