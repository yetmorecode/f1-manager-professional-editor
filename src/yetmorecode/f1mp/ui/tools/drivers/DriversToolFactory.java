package yetmorecode.f1mp.ui.tools.drivers;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class DriversToolFactory extends AbstractToolFactory {

	public DriversToolFactory(F1Model model) {
		super(model);
	}

	@Override
	public String getMenuLabel() {
		return "Drivers";
	}

	@Override
	protected JPanel createJPanel() {
		return new DriversToolPanel(model.driverFile, model.drivers);
	}
	
}
