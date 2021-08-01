package yetmorecode.f1mp.model.history;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class Track {

	public static final int SIZE = 0xb8;
	
	int index;
	
	public long fileOffset;
	public String shortname;
	public String country;
	public int laps;
	public int lapLength;
	public String poleDriver;
	public String poleTeam;
	public String poleTime;
	public String poleAvgSpeed;
	public String fastestLapDriver;
	public String fastestLapTeam;
	public String fastestLapTime;
	public String fastestLapAvgSpeed;
	public String winnerTime;
	public String winnerAvgSpeed;
	
	public static Track createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var track= new Track();
		track.fileOffset = offset;
		track.shortname = new String(input.readNBytes(4));
		track.country = new String(input.readNBytes(20));
		track.laps = input.readInt();
		track.lapLength = input.readInt();
		track.poleDriver = new String(input.readNBytes(20));
		track.poleTeam = new String(input.readNBytes(20));
		track.poleTime = new String(input.readNBytes(12));
		track.poleAvgSpeed = new String(input.readNBytes(12));
		track.fastestLapDriver = new String(input.readNBytes(20));
		track.fastestLapTeam = new String(input.readNBytes(20));
		track.fastestLapTime = new String(input.readNBytes(12));
		track.fastestLapAvgSpeed = new String(input.readNBytes(12));
		track.winnerTime = new String(input.readNBytes(12));
		track.winnerAvgSpeed = new String(input.readNBytes(12));
		input.position(old);
		return track;
	}
	
	public String toString() {
		return country;
	}
	
}
