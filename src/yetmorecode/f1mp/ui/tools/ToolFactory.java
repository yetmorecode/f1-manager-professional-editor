package yetmorecode.f1mp.ui.tools;

import java.io.IOException;

import javax.swing.JPanel;

public interface ToolFactory {
	public String getMenuLabel();
	public JPanel getJPanel() throws IOException;
}
