package yetmorecode.f1mp.model.track;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class TrackPoint {
	public int x;
	public int y;
	public int z;
	
	public static TrackPoint createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var t = new TrackPoint();
		var old = input.position(offset);
		t.x = input.readInt(offset);
		t.y = input.readInt(offset + 4);
		t.z = input.readInt(offset + 8);
		input.position(old);
		return t;
	}
}
