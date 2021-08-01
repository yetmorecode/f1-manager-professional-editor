package yetmorecode.f1mp.model.track;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class TrackCamera {
	public int id;
	public int position;
	
	public static TrackCamera createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var t = new TrackCamera();
		var old = input.position(offset);
		t.id = input.readInt(offset);
		t.position = input.readInt(offset + 4);
		input.position(old);
		return t;
	}
}
