package yetmorecode.f1mp.model.rsc;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.pcx.RGBColor;

public class SpriteResource {
	public long fileOffset;
	public long fileSize;
	
	public int totalItems;
	public int width;
	public int height;
	
	public ArrayList<ArrayList<RGBColor>> items = new ArrayList<>();
	
	public static SpriteResource createFrom(BinaryFileInputStream input, long offset, ArrayList<RGBColor> palette) throws IOException {
		var old = input.position(offset);
		var r = new SpriteResource();
		var fileSize = 0;
		r.fileOffset = offset;
		// usually equal to 0x1 for sprites
    	input.readInt();
    	r.width = input.readInt();
    	r.height = input.readInt();
    	r.totalItems = input.readInt();
    	fileSize += 4*4;
    	for (int item = 0; item < r.totalItems; item++) {
        	int size = input.readInt();
        	var pixels = new ArrayList<RGBColor>();
    		for (int i = 0; i < size; i++) {
        		int color = input.read();
        		fileSize++;
        		if (color == 0) {
        			int skip = input.read();
        			fileSize++;
        			i++;
        			for (int j = 0; j < skip; j++) {
        				var c = palette.get(0);
            			pixels.add(new RGBColor(c.r << 2, c.g << 2, c.b << 2, true));	
            		}
        		} else {
        			var c = palette.get(color);
        			pixels.add(new RGBColor(c.r << 2, c.g << 2, c.b << 2));
        		}
        	}
    		r.items.add(pixels);
    	}
    	r.fileSize = fileSize;
		input.position(old);
		return r;
	}
}
