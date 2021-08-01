package yetmorecode.f1mp.ui.tools.sponsors;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import yetmorecode.f1mp.model.F1Model;
import yetmorecode.f1mp.model.rsc.PCXResource;
import yetmorecode.f1mp.model.rsc.PaletteResource;
import yetmorecode.f1mp.model.rsc.SpriteResource;
import yetmorecode.f1mp.model.sponsor.Sponsor;
import yetmorecode.f1mp.ui.components.filemap.FileMapRegion;
import yetmorecode.f1mp.ui.tools.AbstractFileToolPanel;
import yetmorecode.f1mp.ui.tools.rsc.panels.screen.ScreenPanel;
import yetmorecode.file.BinaryFileInputStream;

public class SponsorsToolPanel extends AbstractFileToolPanel {

	private static final long serialVersionUID = 1457471199152801120L;

	private F1Model model;
	
	private ArrayList<Integer> x = new ArrayList<>();
	private ArrayList<Integer> y = new ArrayList<>();
	
	public SponsorsToolPanel(F1Model model) {
		super(model.sponsorFile.getAbsolutePath(), model.sponsorFile.getAbsolutePath());
		this.model = model;
		for (int i = 0; i < model.sponsors.size(); i++) {
			var t = model.sponsors.get(i);
			
			var node = new DefaultMutableTreeNode(t);
			top.add(node);
			
			map.addKnownRegion(new FileMapRegion(t.fileOffset, t.fileOffset + Sponsor.SIZE));
		}
		
		BinaryFileInputStream in;
		try {
			in = new BinaryFileInputStream(model.exeFile);
			for (int i = 0; i < 56; i++) {
				var pos = 0x208fd6 + i * 0x41;
				in.position(model.exe.getOffsetInFile(0, 0x170010, pos));
				y.add(in.readInt());
				in.position(model.exe.getOffsetInFile(0, 0x170010, pos + 5));
				x.add(in.readInt());
				
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		treeModel.reload();
	}
	
	@Override
	protected void onTreeSelected(DefaultMutableTreeNode node) {
		Object data = node.getUserObject();
		if (data instanceof Sponsor) {
			var h = (Sponsor)data;
			map.highlightRegion(h.fileOffset);
			
			var v = new ScreenPanel(640, 480);
			try {
				var input = new BinaryFileInputStream(model.f1eFile);
				var palette = PaletteResource.createFrom(input, 10253195);
				//var pcx = PCXResource.createFrom(model.f1eFile, 21549933);
				var pcx = PCXResource.createFrom(model.f1eFile, 21022439);
				
				v.addPCX(pcx, 0, 0);
				
				for (int i = 4; i < 60; i++) {
					var sprite = SpriteResource.createFrom(input, h.spriteOffsets.get(i), palette.colors);
					v.addSprite(sprite, x.get(i-4), y.get(i-4));
				}
				
				
				setToolContent(v);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else {
			map.highlightRegion(-1);
		}
	}

}
