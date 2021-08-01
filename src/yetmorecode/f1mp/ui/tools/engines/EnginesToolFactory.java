package yetmorecode.f1mp.ui.tools.engines;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class EnginesToolFactory extends AbstractToolFactory {

	public EnginesToolFactory(F1Model model) {
		super(model);
	}

	@Override
	public String getMenuLabel() {
		return "Engines";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new EnginesToolPanel(model);
	}

}
