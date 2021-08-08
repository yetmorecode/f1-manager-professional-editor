package yetmorecode.f1mp.ui.tools.exe;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.engine.Engine;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;
import yetmorecode.file.format.lx.LeObjectPageTableEntry;
import yetmorecode.file.format.lx.LxExecutable;
import yetmorecode.file.format.lx.LxHeader;
import yetmorecode.file.format.lx.ObjectTableEntry;
import yetmorecode.file.format.mz.MzHeader;

public class LinearExecutablePanel extends AbstractFileToolPanel {

	private class PageNode {
		public int index;
		public LeObjectPageTableEntry entry;
		
		PageNode(int index, LeObjectPageTableEntry entry) {
			this.index = index;
			this.entry = entry;
		}
		
		public String toString() {
			return "Page #" + index;
		}
	}
	
	private static final long serialVersionUID = 2717387818984301657L;

	private File exeFile;
	private LxExecutable exe;
	
	public LinearExecutablePanel(File file, LxExecutable exe) {
		super(file.getAbsolutePath(), file.getAbsolutePath());
		
		this.exeFile = file;
		this.exe = exe;
		
		top.add(new DefaultMutableTreeNode(exe.dosHeader));
		map.addKnownRegion(new FileMapRegion(0, MzHeader.SIZE));
		
		top.add(new DefaultMutableTreeNode(exe.header));
		map.addKnownRegion(new FileMapRegion(exe.dosHeader.fileAddressNewExe, exe.dosHeader.fileAddressNewExe + LxHeader.SIZE));
		
		for (int i = 0; i < exe.objectTable.size(); i++) {
			
			var object = exe.objectTable.get(i);
			
			var objectNode = new DefaultMutableTreeNode(object);
			top.add(objectNode);
			
			for (int j = 0; j < object.pageCount; j++) {
				var index = object.pageTableIndex + j - 1;
				var page = exe.objectPageTable.get(index);
				
				var pageNode = new DefaultMutableTreeNode(new PageNode(index, (LeObjectPageTableEntry) page));
				objectNode.add(pageNode);
				
				var fixups = exe.fixupRecordTable.get(index);
				for (int k = 0; k < fixups.size(); k++) {
					var fixupNode = new DefaultMutableTreeNode(fixups.get(k));
					pageNode.add(fixupNode);
				}
				
				var pageSize = exe.header.pageSize;
				if (page.getOffset() == exe.header.pageCount) {
					pageSize = exe.header.lastPageSize;
				}
				
				map.addKnownRegion(new FileMapRegion(
					exe.header.dataPagesOffset + index * exe.header.pageSize, 
					exe.header.dataPagesOffset + index * exe.header.pageSize + pageSize
				));
			}
			
			var offset = exe.header.pageTableOffset + i * ObjectTableEntry.SIZE;
			map.addKnownRegion(new FileMapRegion(offset, offset + ObjectTableEntry.SIZE));
		}
		treeModel.reload();
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof MzHeader) {
			var d = (MzHeader)data;
			map.highlightRegion(0);
			//setToolContent(new EnginePanel(h, rscFile));
		} else if (data instanceof LxHeader) {
			var d = (LxHeader)data;
			map.highlightRegion(exe.dosHeader.fileAddressNewExe);
		} else if (data instanceof PageNode) {
			var d = (PageNode)data;
			map.highlightRegion(exe.header.dataPagesOffset + (d.entry.getOffset()-1) * exe.header.pageSize);
		} else {
			map.highlightRegion(-1);
		}
	}

}
