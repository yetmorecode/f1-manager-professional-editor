package yetmorecode.f1mp.model.driver;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class Driver {
	public static final long SIZE = 568; 
	
	public long fileOffset;
	public String name;
	
	public static Driver createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var d = new Driver();
		
		long old = input.position(offset);
		d.fileOffset  = offset;
		d.name = input.readString(20).trim();
		input.position(old);
		
		return d;
	}
	
	public String toString() {
		return name;
	}
}
