package yetmorecode.f1mp.ui.tools.points;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class PointsToolFactory extends AbstractToolFactory {

	public PointsToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "Race Points";
	}

	@Override
	protected JPanel createJPanel() {
		return new PointsToolPanel(model.gameDirectory + "/F1.exe");
	}

}
