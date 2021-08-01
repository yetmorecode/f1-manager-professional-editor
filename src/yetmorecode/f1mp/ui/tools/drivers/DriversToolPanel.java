package yetmorecode.f1mp.ui.tools.drivers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.driver.Driver;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;

public class DriversToolPanel extends AbstractFileToolPanel {

	private static final long serialVersionUID = -5452758205432702175L;

	public DriversToolPanel(File file, ArrayList<Driver> drivers) {
		super(file.getAbsolutePath(), file.getAbsolutePath());
		for (int i = 0; i < drivers.size(); i++) {
			var t = drivers.get(i);
			
			var node = new DefaultMutableTreeNode(t);
			top.add(node);
			
			map.addKnownRegion(new FileMapRegion(t.fileOffset, t.fileOffset + Driver.SIZE));
		}
		treeModel.reload();
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof Driver) {
			var h = (Driver)data;
			map.highlightRegion(h.fileOffset);
			//setToolContent(new Spons(h));
		} else {
			map.highlightRegion(-1);
		}
	}

}
