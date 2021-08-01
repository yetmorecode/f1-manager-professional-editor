package yetmorecode.f1mp.model.rsc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.pcx.PCX;
import yetmorecode.file.format.pcx.RGBColor;
import yetmorecode.file.format.pcx.VGAPalette;

public class PCXResource extends PCX {
	public long fileOffset;
	public long fileSize;
	public boolean paletteAppended;
	
	public static PCXResource createFrom(File file, long offset) throws IOException {
		return createFrom(new BinaryFileInputStream(file), offset);
	}
	
	public static PCXResource createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var r = new PCXResource();
		r.fileOffset = offset;
		r.fileSize = input.readInt();
		int b = input.readByte(offset + 4 + r.fileSize - VGAPalette.SIZE - 1);
		if (b == 0xc) {
			var p = VGAPalette.createFrom(input, offset + 4 + r.fileSize - VGAPalette.SIZE);
			p.colors.set(255, new RGBColor(0, 0, 0));
			p.colors.set(0, new RGBColor(0, 0, 0));
			p.colors.set(192, new RGBColor(0x3f << 2, 0x3f << 2, 0x3f << 2));
			r.paletteAppended = true;
			PCX.createFrom(input, offset + 4, p.colors, r);	
		} else {
			r.paletteAppended = false;
			PCX.createFrom(input, offset + 4, new ArrayList<>(), r);
		}
		
		input.position(old);
		return r;
	}
}
