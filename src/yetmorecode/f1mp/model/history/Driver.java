package yetmorecode.f1mp.model.history;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class Driver {

	public static final int SIZE = 0x120;
	
	public long fileOffset;
	public int index;
	public String name;
	public String shortname;
	public String team;
	public int teamId;
	public int seasonPoints;
	public int[] racePoints = new int[17];
	public int unknown1;
	public int unknown2;
	public int[] racePositions = new int[17];
	
	public static Driver createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var d = new Driver();
		d.fileOffset = offset;
		d.name = new String(input.readNBytes(40));
		d.shortname = new String(input.readNBytes(20));
		d.team = new String(input.readNBytes(4));
		d.teamId = input.readInt();
		d.seasonPoints = input.readInt();
		for (int i = 0; i < 17; i++) {
			d.racePoints[i] = input.readInt();
		}
		d.unknown1 = input.readInt();
		d.unknown2 = input.readInt();
		for (int i = 0; i < 17; i++) {
			d.racePositions[i] = input.readInt();
		}
		input.position(old);
		return d;
	}
	
	public String toString() {
		return name;
	}
}
