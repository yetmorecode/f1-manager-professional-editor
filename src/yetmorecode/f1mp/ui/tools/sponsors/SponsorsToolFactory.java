package yetmorecode.f1mp.ui.tools.sponsors;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class SponsorsToolFactory extends AbstractToolFactory {

	public SponsorsToolFactory(F1Model model) {
		super(model);
	}

	@Override
	public String getMenuLabel() {
		return "Sponsors";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new SponsorsToolPanel(model);
	}

}
