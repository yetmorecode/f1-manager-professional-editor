package yetmorecode.f1mp.ui.tools.teams;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class TeamsToolFactory extends AbstractToolFactory {

	public TeamsToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "Teams";
	}

	@Override
	protected JPanel createJPanel() {
		return new TeamsToolPanel(model.teamFile, model.teams);
	}

}
