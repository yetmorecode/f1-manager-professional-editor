package yetmorecode.f1mp.model.rsc;

import java.io.IOException;
import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.pcx.RGBColor;
import yetmorecode.file.format.pcx.VGAPalette;

public class PaletteResource extends VGAPalette {
	public long fileOffset;
	public long fileSize;
	
	public static PaletteResource createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var p = new PaletteResource();
		p.fileOffset = offset;
		p.fileSize = input.readInt();
		VGAPalette.createFrom(input, offset + 4, p);

		if (offset == 0x1014d36) {
			p.colors.set(192, new RGBColor(0x3f << 2, 0x3f << 2, 0x3f << 2));
		}
		if (offset == 0x9c768f || offset == 0x1014d36) {
			p.colors.set(0, new RGBColor(0, 0, 0));
		}
	
		input.position(old);
		return p;
	}
	
}
