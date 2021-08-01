package yetmorecode.f1mp.ui.tools;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;

public abstract class AbstractToolFactory implements ToolFactory {

	private JPanel panel;
	protected F1Model model;
	
	protected abstract JPanel createJPanel() throws IOException;
	
	public AbstractToolFactory(F1Model model) {
		this.model = model;
	}
	
	@Override
	public JPanel getJPanel() throws IOException {
		if (panel == null) {
			panel = createJPanel();
		}
		return panel;
	}

}
