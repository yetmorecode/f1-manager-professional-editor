package yetmorecode.f1mp.model.engineer;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class Engineer {
	public static final long SIZE = 148; 
	
	public long fileOffset;
	public String name;
	
	public static Engineer createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var d = new Engineer();
		
		long old = input.position(offset);
		d.fileOffset  = offset;
		d.name = input.readString(40).trim();
		input.position(old);
		
		return d;
	}
	
	public String toString() {
		return name;
	}
}
