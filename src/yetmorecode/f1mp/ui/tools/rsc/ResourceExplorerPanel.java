package yetmorecode.f1mp.ui.tools.rsc;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import yetmorecode.f1mp.model.rsc.ResourceDefinition;
import yetmorecode.f1mp.model.rsc.SpriteResource;
import yetmorecode.f1mp.ui.components.filemap.FileMapPanel;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.rsc.panels.PCXPanel;
import yetmorecode.f1mp.ui.tools.rsc.panels.PalettePanel;
import yetmorecode.f1mp.ui.tools.rsc.panels.SpriteImagePanel;
import yetmorecode.file.BinaryFileInputStream;

public class ResourceExplorerPanel extends JPanel implements TreeSelectionListener {
	private static final long serialVersionUID = 8351179612739742622L;

	private JTree tree;
	private JScrollPane contentScroll;
	private JLabel content;
	private JSplitPane splitPane;
	private JPanel searchPanel;
	private FileMapPanel map;
	
	private JTextField search;
	
	private DefaultMutableTreeNode top;
	private String file;
	
	private ArrayList<ResourceDefinition> resources;
	
	public ResourceExplorerPanel(String filename) {
		setLayout(new BorderLayout());
		
		file = filename;
		resources = new ArrayList<>();
		
		createTree();
		createSearch();
	    
	    
	    JScrollPane treeScroll = new JScrollPane(tree);
	    treeScroll.setBorder(null);
	    JPanel treePanel = new JPanel();
	    treePanel.setLayout(new BorderLayout());
	    treePanel.add(searchPanel, BorderLayout.NORTH);
	    treePanel.add(treeScroll);
	    
	    
	    
	    
	    content = new JLabel("");
	    contentScroll = new JScrollPane(content);
	    contentScroll.setBorder(null);
	    contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    
	    var contentWrap = new JPanel();
	    contentWrap.setLayout(new BorderLayout());
	    
	    map = new FileMapPanel(filename);
	    contentWrap.add(map, BorderLayout.NORTH);
	    contentWrap.add(contentScroll);
	    
	    
	    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    splitPane.setLeftComponent(treePanel);
	    splitPane.setRightComponent(contentWrap);
	    splitPane.setDividerLocation(350);
	    
	    //Provide minimum sizes for the two components in the split pane
	    //Dimension minimumSize = new Dimension(100, 50);
	    //treeScroll.setMinimumSize(minimumSize);
	    //contentScroll.setMinimumSize(minimumSize);
	    
	    
	    
	    defineResources();
	    
	    try {
			var input = new BinaryFileInputStream(filename);
			for (ResourceDefinition r : resources) {
				input.getChannel().position(r.getOffset());
				int size = input.readInt();
				r.setSize(size);
				map.addKnownRegion(new FileMapRegion(r.getOffset(), r.getOffset() + size));
				
				
				top.add(new DefaultMutableTreeNode(r));
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    model.reload();
	    
	    add(splitPane);
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		var node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null) {     
			return;
		}

		Object nodeInfo = node.getUserObject();
		if (node.isLeaf()  && !(nodeInfo instanceof String)) {
			ResourceDefinition r = (ResourceDefinition)nodeInfo;
			updateResource(r);
		}
	}
	
	private void updateResource(ResourceDefinition r) {
		content.setText(r.getName());
		try {
			if (r.getType() == ResourceDefinition.Type.TYPE_PALETTE) {
				var v = new PalettePanel(file, r.getOffset());
				setResourceContent(r, v);
			} else if (r.getType() == ResourceDefinition.Type.TYPE_PCX) {
				var v = new PCXPanel(file, r.getOffset());
				setResourceContent(r, v);
			} else if (r.getType() == ResourceDefinition.Type.TYPE_SPRITE) {
				var p = new PalettePanel(file, r.getPaletteOffset());
				var input = new BinaryFileInputStream(file);
				var sprite = SpriteResource.createFrom(input, r.getOffset(), p.palette.colors);
				var v = new SpriteImagePanel(sprite);
				setResourceContent(r, v);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void setResourceContent(ResourceDefinition r, JPanel panel) {
		//map.addKnownRegion(new FileMapRegion(r.getOffset(), r.getOffset() + r.getSize()));
		if (r != null) {
			map.highlightRegion(r.getOffset());	
		}
		
		contentScroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
		        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentScroll.setBorder(new EmptyBorder(10, 5, 5, 0));
		contentScroll.getVerticalScrollBar().setUnitIncrement(16);
		
		var contentWrap = new JPanel();
	    contentWrap.setLayout(new BorderLayout());
	    contentWrap.add(map, BorderLayout.NORTH);
	    contentWrap.add(contentScroll);
		
	    var div = splitPane.getDividerLocation();
		splitPane.setRightComponent(contentWrap);
		splitPane.setDividerLocation(div);
	}
	
	private void createTree() {
		top = new DefaultMutableTreeNode("f1_e.rsc");
	    tree = new JTree(top);
	    tree.setBackground(null);
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(this);
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    model.setRoot(top);
	    
	}
	
	private void createSearch() {
		searchPanel = new JPanel();
	    searchPanel.setBorder(new EmptyBorder(10, 5, 10, 0));
	    searchPanel.setLayout(new GridLayout(0, 1));
	    search = new JTextField();
	    searchPanel.add(search);
	    search.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
						
					String offstr = search.getText();
					long offset = 0;
					if (offstr.startsWith("0x")) {
						offstr = offstr.replaceAll("0x", "");
						offset = Long.parseLong(offstr, 16);
					} else {
						offset = Long.parseLong(offstr, 10);
					}
					
					try {
						
						var v = new PCXPanel(file, offset);
						setResourceContent(null, v);
						/*
						var v = new SpriteImagePanel(SpriteResource.createFrom(new BinaryFileInputStream(file), offset, p.palette.colors));
						contentScroll = new JScrollPane(v, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
						        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						splitPane.setRightComponent(contentScroll);
						*/
					} catch(Exception ex) {
						System.out.println(ex.getMessage());
						PalettePanel p;
						try {
							p = new PalettePanel(file, 0x9c738b);
							var v = new SpriteImagePanel(SpriteResource.createFrom(new BinaryFileInputStream(file), offset, p.palette.colors));
							setResourceContent(null, v);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}				
		        }
			}
		});
	}
	
	private void definePCX(long offset, String name, long palette, long[] refs) {
		resources.add(new ResourceDefinition(offset, name, ResourceDefinition.Type.TYPE_PCX, palette, refs));
	}
	
	private void defineResources() {
		
		definePCX(0xa76d4d, "mainmenu.background",  0x1014d36, new long[] { 0x1 });
		
		resources.add(new ResourceDefinition(0x103c56e, "mainmenu.logo", 		ResourceDefinition.Type.TYPE_SPRITE, 0x1014d36));
		
		resources.add(new ResourceDefinition(0x6316d0, "general.a", 		ResourceDefinition.Type.TYPE_PCX, 0x9c4047));
		resources.add(new ResourceDefinition(0x6625af, "general.b", 		ResourceDefinition.Type.TYPE_PCX, 0x9c434b));
		resources.add(new ResourceDefinition(0xafd2ee, "general.end", 	ResourceDefinition.Type.TYPE_PCX, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x69d750, "general.cryface", ResourceDefinition.Type.TYPE_PCX, 0x9c464f));
		resources.add(new ResourceDefinition(0x5cd5ad, "general.crew", 	ResourceDefinition.Type.TYPE_PCX, 0x9c3d43));
		resources.add(new ResourceDefinition(0xafd2ee, "general.end2", 	ResourceDefinition.Type.TYPE_PCX, 0x9c3a3f));
		
		resources.add(new ResourceDefinition(0x4379b8, "merch.background", 	ResourceDefinition.Type.TYPE_PCX, 0x1014d36));
		resources.add(new ResourceDefinition(0xc3fae0, "merch.foo", 			ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x225148, "merch.poster1", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x2343a6, "merch.poster2", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x23821c, "merch.poster3", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x21c13c, "merch.poster4", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x2238ef, "merch.poster5", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22bfc9, "merch.poster6", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22fe7d, "merch.poster7", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x2306df, "merch.poster8", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x23b3bd, "merch.poster9", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22eba3, "merch.poster10", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22944a, "merch.poster11", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x23197d, "merch.poster12", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x233802, "merch.poster13", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22b719, "merch.poster14", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x236a3a, "merch.poster15", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x221b9e, "merch.poster16", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x222d25, "merch.poster17", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x21fb77, "merch.poster18", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x22244d, "merch.poster19", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x23049a, "merch.poster20", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		resources.add(new ResourceDefinition(0x227b93, "merch.puppet", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c3a3f));
		
		resources.add(new ResourceDefinition(46008, "track.melbourne", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c5563));
		resources.add(new ResourceDefinition(57494, "track.melbourne2", 		ResourceDefinition.Type.TYPE_SPRITE, 0x9c5563));
	    
		/*
	    // unsorted
	    addSprite(p, 0x5f5bc, 0x1014d36, "Mouse cursor loading");
	    addSprite(p, 0xafffb3, 0x1014d36, "Mouse cursor help");
	    addSprite(p, 0xbb7891, 0x1014d36, "Mouse cursor car");
	    addSprite(p, 0x12b6c29, 0x1014d36, "Button Load");
	    addSprite(p, 0x12b7d8d, 0x1014d36, "Button Save");
	    addSprite(p, 0x12b8ef1, 0x1014d36, "Button Preset");
	    addSprite(p, 0x12bd481, 0x1014d36, "Button Back");
	    addSprite(p, 0x12bb1b9, 0x1014d36, "Button Course");
	    addSprite(p, 0x12c08ad, 0x1014d36, "Car number 1");
	    addSprite(p, 0x12c0c09, 0x1014d36, "Car number 2");
	    addSprite(p, 0x12c0f65, 0x1014d36, "Car number T");
	    addSprite(p, 0x7af53, 0x1014d36, "Car number T");
	    addSprite(p, 0x7b198, 0x1014d36, "Car number T");
	    addSprite(p, 0x7aba0, 0x1014d36, "Car number T");
	    addSprite(p, 0x7acb0, 0x1014d36, "Car number T");
	    addSprite(p, 0x7ad42, 0x1014d36, "Car number T");
	    addSprite(p, 0x7b3c2, 0x1014d36, "Car number T");
	    addSprite(p, 0x7b4fa, 0x1014d36, "Car number T");
	    addSprite(p, 0x7b6b3, 0x1014d36, "Car number T");
	    addSprite(p, 0x12c12c1, 0x1014d36, "Wing needle");
	    addSprite(p, 0x12cbfdd, 0x1014d36, "Wing needle");
	    addSprite(p, 0x12cc12d, 0x1014d36, "Wing needle");
	    addSprite(p, 0x12cc27d, 0x1014d36, "Wing needle");
	    addSprite(p, 0x12e4005, 0x1014d36, "Suspension pictogram");
	    addSprite(p, 0x1542225, 0x1014d36, "Button New Player enabled");
	    addSprite(p, 0x1546995, 0x1014d36, "Button New Player disabled");
	    addSprite(p, 0x1547585, 0x1014d36, "Button Delete Player disabled");
	    addSprite(p, 0x1548175, 0x1014d36, "Button Laptimes disabled");
	    
	    
	    // main menu first stage
	    var mainmenu = new DefaultMutableTreeNode("Mainmenu");
	    top.add(mainmenu);
	    mainmenu.add(new DefaultMutableTreeNode());
	    addSprite(mainmenu, 0x11a97d6, 0x1014d36, "Number of players");
		addSprite(mainmenu, 0x1fcb21f, 0x1014d36, "Intro");
		addSprite(mainmenu, 0x105bcee, 0x1014d36, "Credits");					
		addSprite(mainmenu, 0x104eb32, 0x1014d36, "Load Saved Game");
		addSprite(mainmenu, 0x11b6992, 0x1014d36, "Quit Game");
		addSprite(mainmenu, 0x103c56e, 0x1014d36, "Logo F1 Manager Professional");
		addSprite(mainmenu, 0x103e890, 0x1014d36, "Start Game");
		addSprite(mainmenu, 0x103f2ae, 0x1014d36, "Right Arrows / Next");
		addSprite(mainmenu, 0x10aac11, 0x1014d36, "Slider Bar");
		addSprite(mainmenu, 0x10ab11e, 0x1014d36, "Slider Handle");
		addSprite(mainmenu, 0x0af20d0, 0x1014d36, "Nothing?");
		addSprite(mainmenu, 0x101503a, 0x1014d36, "Top Team");
		addSprite(mainmenu, 0x11d608c, 0x1014d36, "Player1 (gold)");
		addSprite(mainmenu, 0x11c48bc, 0x1014d36, "Player1");
		addSprite(mainmenu, 0x1842a28, 0x1014d36, "ok cancel (todo)");
	    addSprite(mainmenu, 0x11a97d6, 0x1014d36, "Number of Players");
		addSprite(mainmenu, 0x11d608c, 0x1014d36, "Editor");
		addSprite(mainmenu, 0x1041976, 0x1014d36, "Editor");
		addSprite(mainmenu, 0x1069dad, 0x1014d36, "Editor");
		addSprite(mainmenu, 0x26f754, 0x1014d36, "Editor");
		*/
		
	    resources.add(new ResourceDefinition(0x20b0398, "Background drivers", 	ResourceDefinition.Type.TYPE_PCX, 0x2126203));
	    resources.add(new ResourceDefinition(0x20d82c7, "Background engineers", 	ResourceDefinition.Type.TYPE_PCX, 0x2126203));
	    resources.add(new ResourceDefinition(0x2100148, "Background teams", 		ResourceDefinition.Type.TYPE_PCX, 0x2126203));
	    resources.add(new ResourceDefinition(0x213007d, "Alphabet", 				ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    resources.add(new ResourceDefinition(0x21337ca, "Button arrow right", 	ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    resources.add(new ResourceDefinition(0x213396a, "Button arrow left", 		ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    resources.add(new ResourceDefinition(0x87251, "Flags", 					ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    resources.add(new ResourceDefinition(0x2134722, "Car numbers", 			ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    resources.add(new ResourceDefinition(0x213a67b, "Button (red)", 			ResourceDefinition.Type.TYPE_SPRITE, 0x2126203));
	    //new Resource(0x40, "alphabet", Resource.Type.TYPE_SPRITE, 0x2126203)));;
	}
}
