package yetmorecode.f1mp.ui.tools.exe;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class ExecutableToolFactory extends AbstractToolFactory {

	public ExecutableToolFactory(F1Model directory) {
		super(directory);
	}

	@Override
	public String getMenuLabel() {
		return "LE-executable (F1.exe)";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new JPanel();
	}

}
