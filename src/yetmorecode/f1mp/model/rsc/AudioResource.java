package yetmorecode.f1mp.model.rsc;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class AudioResource {
	public long fileOffset;
	public long fileSize;
	public byte[] data;
	
	public static AudioResource createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var a = new AudioResource();
		a.fileOffset = offset;
		a.fileSize = input.readInt();
		a.data = input.readNBytes((int) a.fileSize);
		input.position(old);
		return a;
	}
}
