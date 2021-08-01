package yetmorecode.f1mp.ui.tools.engines;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.model.engine.Engine;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;

public class EnginesToolPanel extends AbstractFileToolPanel {

	private static final long serialVersionUID = -8837648106470717133L;
	
	private File rscFile; 
	
	public EnginesToolPanel(F1Model model) {
		super(model.engineFile.getAbsolutePath(), model.engineFile.getAbsolutePath());
		rscFile = model.f1eFile;
		for (int i = 0; i < model.engines.size(); i++) {
			var t = model.engines.get(i);
			
			var node = new DefaultMutableTreeNode(t);
			top.add(node);
			
			map.addKnownRegion(new FileMapRegion(t.fileOffset, t.fileOffset + Engine.SIZE));
		}
		treeModel.reload();
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof Engine) {
			var h = (Engine)data;
			map.highlightRegion(h.fileOffset);
			setToolContent(new EnginePanel(h, rscFile));
		} else {
			map.highlightRegion(-1);
		}
	}

}
