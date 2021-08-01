package yetmorecode.f1mp.model.team;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class Team {
	public static final long SIZE = 1212; 
	
	public long fileOffset;
	public String name;
	
	public static Team createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var d = new Team();
		
		long old = input.position(offset);
		d.fileOffset  = offset;
		input.readInt();
		d.name = input.readString(40).trim();
		input.position(old);
		
		return d;
	}
	
	public String toString() {
		return name;
	}
}
