package yetmorecode.f1mp.ui.tools.calendar;

import java.io.IOException;

import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.AbstractToolFactory;

public class CalendarToolFactory extends AbstractToolFactory {

	public CalendarToolFactory(F1Model model) {
		super(model);
	}

	@Override
	public String getMenuLabel() {
		return "Season Calendar";
	}

	@Override
	protected JPanel createJPanel() throws IOException {
		return new CalendarPanel(model);
	}

}
