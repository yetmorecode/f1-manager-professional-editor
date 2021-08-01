package yetmorecode.f1mp.model.track;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class TrackSegment {
	public int x;
	public int y;
	public int z;
	public float a;
	public float b;
	
	public static TrackSegment createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var t = new TrackSegment();
		var old = input.position(offset);
		t.x = input.readInt(offset);
		t.y = input.readInt(offset + 4);
		t.z = input.readInt(offset + 8);
		t.a = input.readFloat(offset + 12);
		t.b = input.readFloat(offset + 16);
		input.position(old);
		return t;
	}

}
