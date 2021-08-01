package yetmorecode.f1mp.ui.tools.history;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class HistoryToolFactory extends AbstractToolFactory {

	public HistoryToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "Season statistics";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new HistoryToolPanel(model.gameDirectory + "\\SAVE\\HISTORY.DAT");
	}

}
