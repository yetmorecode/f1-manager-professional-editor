package yetmorecode.f1mp.ui.tools.tracks;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class TracksToolFactory extends AbstractToolFactory {

	public TracksToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "Track models";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new TracksToolPanel(model.gameDirectory + "\\DATA97_E\\STRECKEN.DAT");
	}

}
