package yetmorecode.f1mp.ui.tools.rsc.panels;

import java.io.IOException;
import yetmorecode.f1mp.model.rsc.PCXResource;
import yetmorecode.f1mp.ui.tools.rsc.panels.screen.ScreenPanel;

public class PCXImagePanel extends ScreenPanel {
	private static final long serialVersionUID = 4470777298821205654L;
	
	public PCXImagePanel(String file, PCXResource pcx) throws IOException {
		super(pcx.getWidth(), pcx.getHeight());
		addPCX(pcx, 0, 0);
	}
	
}
