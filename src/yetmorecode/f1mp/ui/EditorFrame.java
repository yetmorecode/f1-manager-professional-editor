package yetmorecode.f1mp.ui;

import java.awt.CardLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.ui.tools.ToolFactory;
import yetmorecode.f1mp.ui.tools.drivers.DriversToolFactory;
import yetmorecode.f1mp.ui.tools.engines.EnginesToolFactory;
import yetmorecode.f1mp.ui.tools.exe.ExecutableToolFactory;
import yetmorecode.f1mp.ui.tools.history.HistoryToolFactory;
import yetmorecode.f1mp.ui.tools.points.PointsToolFactory;
import yetmorecode.f1mp.ui.tools.rsc.ResourceExplorerToolFactory;
import yetmorecode.f1mp.ui.tools.sponsors.SponsorsToolFactory;
import yetmorecode.f1mp.ui.tools.teams.TeamsToolFactory;
import yetmorecode.f1mp.ui.tools.tracks.TracksToolFactory;

public class EditorFrame extends AppFrame {
	

	private static final long serialVersionUID = -5273111732367009662L;
	
	private final static int WIDTH = 1280;
	private final static int HEIGHT = 800;
	private final static String PREF_GAME_DIRECTORY = "GAME_DIRECTORY";
	private final static String PREF_LAST_TOOL = "LAST_TOOL";
	private final static String TITLE = "F1 Manager Professional Editor";
	
	private JMenu fileMenu;
	//private JMenu viewMenu;
	private JMenu toolsMenu;
	private JMenu helpMenu;
	
	private JPanel content;
	private CardLayout cardLayout;
	
	private String gameDirectory;
	private F1Model model;
	
	private ArrayList<ToolFactory> toolFactories;
	
	public EditorFrame() {
		super(TITLE, WIDTH, HEIGHT);
		
		// Let user select game directory if not selected previously
	    if (prefs.get(PREF_GAME_DIRECTORY, "").equals("")) {
	    	selectGameDirectory();
	    }
	    gameDirectory = prefs.get(PREF_GAME_DIRECTORY, "");
	    try {
			model = F1Model.loadFrom(gameDirectory);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    // Setup basic app ui
	    setupTools();
	    createMenubar();
	    createStatusbar();
	    setStatus(gameDirectory);
	    
	    //setIconImage(new ImageIcon("res/image/icon.png").getImage());
	    
	    // Add all tools to a card layout
	    content = new JPanel();
	    cardLayout = new CardLayout();
	    content.setLayout(cardLayout);
	    for (ToolFactory f : toolFactories) {
	    	try {
				content.add(f.getJPanel(), f.getMenuLabel());
			} catch (IOException e) {
				System.out.println("Could not initialize tool: " + f.getMenuLabel());
				//e.printStackTrace();
			}
	    }
	    add(content);
	    
	    // Open last used tool
	    String lastTool = prefs.get(PREF_LAST_TOOL, "");
	    for (ToolFactory f : toolFactories) {
	    	if (lastTool.equals(f.getMenuLabel())) {
	    		openTool(f);
	    	}
	    }
	}
	
	@Override
	protected void setupMenubar(JMenuBar menubar) {
		// File
		fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		var open = new JMenuItem("Open");
		open.addActionListener((event) -> {
			selectGameDirectory();
			gameDirectory = prefs.get(PREF_GAME_DIRECTORY, "");
			try {
				model = F1Model.loadFrom(gameDirectory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		fileMenu.add(open);
		var save = new JMenuItem("Save");
		fileMenu.add(save);
		var saveAs = new JMenuItem("Save As...");
		fileMenu.add(saveAs);
		var quit = new JMenuItem("Exit");
		fileMenu.add(quit);
		
		// View
		//viewMenu = new JMenu("View");
		//menubar.add(viewMenu);
		
		// Tools
		toolsMenu = new JMenu("Tools");
		menubar.add(toolsMenu);
		for (ToolFactory f : toolFactories) {
			var item = new JMenuItem(f.getMenuLabel());
			item.addActionListener((event) -> {
				openTool(f);
			});
			toolsMenu.add(item);
		}
		
		// Help
		helpMenu = new JMenu("Help");
		menubar.add(helpMenu);
		var about = new JMenuItem("About");
		helpMenu.add(about);
	}
	
	private void setupTools() {
		toolFactories = new ArrayList<>();
		toolFactories.add(new PointsToolFactory(model));
	    toolFactories.add(new DriversToolFactory(model));
	    toolFactories.add(new TeamsToolFactory(model));
	    toolFactories.add(new EnginesToolFactory(model));
	    toolFactories.add(new TracksToolFactory(model));
	    toolFactories.add(new SponsorsToolFactory(model));
	    toolFactories.add(new HistoryToolFactory(model));
	    toolFactories.add(new ExecutableToolFactory(model));
	    toolFactories.add(new ResourceExplorerToolFactory(model));
	}
	
	/**
	 * Shows a JFileChooser to let the user pick the game directory
	 */
	private void selectGameDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select F1 Manager Professional game directory");
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int option = chooser.showOpenDialog(EditorFrame.this);
	    if (option == JFileChooser.APPROVE_OPTION) {
	    	prefs.put(PREF_GAME_DIRECTORY, chooser.getSelectedFile().getAbsolutePath());
	    }
	}
	
	private void openTool(ToolFactory f) {
		// Update status bar with tool name
		setStatus(gameDirectory + " - " + f.getMenuLabel());
		
		// Show tools card in the card layout
		cardLayout.show(content, f.getMenuLabel());
		
		// Store last used tool
		prefs.put(PREF_LAST_TOOL, f.getMenuLabel());
	}
}
