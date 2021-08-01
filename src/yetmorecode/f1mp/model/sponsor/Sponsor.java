package yetmorecode.f1mp.model.sponsor;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class Sponsor {
	public static final long SIZE = 432; 
	
	public long fileOffset;
	public String name;
	
	public ArrayList<Long> spriteOffsets = new ArrayList<>();
	
	public static Sponsor createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var s = new Sponsor();
		var old = input.position(offset);
		s.fileOffset = offset;
		s.name = new String(input.readNBytes(32)).trim();
		for (int i = 0; i < 60; i++) {
			var spriteOffset = input.readInt(offset + 192 + i*4);
			s.spriteOffsets.add((long) spriteOffset);
		}
		input.position(old);
		return s;
	}
	
	public String toString() {
		return name;
	}
}
