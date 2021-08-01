package yetmorecode.f1mp.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public abstract class AppFrame extends JFrame {
	private static final long serialVersionUID = -8397473957565691280L;
	
	protected JMenuBar menubar;
	protected JMenu menu;
	protected JPanel statusbar;
	protected JLabel statusLabel;
	
	protected Preferences prefs;
	
	public AppFrame(String title, int width, int height) {
		super();
		setTitle(title);
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	    prefs = Preferences.userRoot().node(this.getClass().getName());
	    setLayout(new BorderLayout());
	}
	
	protected void setupMenubar(JMenuBar menubar) {
	}
	
	protected void setStatus(String status) {
		statusLabel.setText(status);
	}
	
	protected void createMenubar() {
		menubar = new JMenuBar();
		setJMenuBar(menubar);
	    setupMenubar(menubar);
	}
	
	protected void createStatusbar() {
		statusbar = new JPanel();
		add(statusbar, BorderLayout.SOUTH);
	    statusbar.setPreferredSize(new Dimension(getWidth(), 30));
	    statusbar.setLayout(new BoxLayout(statusbar, BoxLayout.X_AXIS));
	    statusbar.setBorder(new EmptyBorder(10, 5, 10, 5));
	    statusLabel = new JLabel("status");
	    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    statusbar.add(statusLabel);
	    
	}
}
