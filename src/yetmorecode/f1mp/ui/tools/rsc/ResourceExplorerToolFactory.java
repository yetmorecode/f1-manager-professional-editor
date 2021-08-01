package yetmorecode.f1mp.ui.tools.rsc;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;


public class ResourceExplorerToolFactory extends AbstractToolFactory {

	public ResourceExplorerToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "f1_e.rsc";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		//var r = new Resource(0x2126203, Resource.Type.TYPE_PALETTE);
		return new ResourceExplorerPanel(model.gameDirectory + "/f1_e.rsc");
	}

}
