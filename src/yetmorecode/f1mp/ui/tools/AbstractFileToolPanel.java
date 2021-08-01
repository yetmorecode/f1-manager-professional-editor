package yetmorecode.f1mp.ui.tools;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

import yetmorecode.f1mp.ui.components.filemap.FileMapPanel;

public class AbstractFileToolPanel extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 835816805181419226L;
	
	protected JTree tree;
	protected DefaultTreeModel treeModel;
	protected DefaultMutableTreeNode top;
	
	protected JScrollPane contentScroll;
	protected JLabel content;
	protected JSplitPane splitPane;
	protected JPanel searchPanel;
	protected FileMapPanel map;
	protected JTextField search;
	
	protected String file;
	
	public AbstractFileToolPanel(String filename, String treeTitle) {
		setLayout(new BorderLayout());
		file = filename;
		
		createTree(treeTitle);
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
	    
	    add(splitPane);
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		var node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null) {     
			return;
		}
		onTreeSelected(node);
	}
	
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		
	}
	
	protected void onSearch(String query) throws Exception {
		
	}
	
	private void createTree(String title) {
		top = new DefaultMutableTreeNode(title);
	    tree = new JTree(top);
	    tree.setBackground(null);
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    tree.addTreeSelectionListener(this);
	    treeModel = (DefaultTreeModel) tree.getModel();
	    treeModel.setRoot(top);
	    
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
					try {
						onSearch(search.getText());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        }
			}
		});
	}
	
	protected void setToolContent( JPanel panel) {
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
	
	
}
