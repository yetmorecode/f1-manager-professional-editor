package yetmorecode.f1mp.model.history;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class SeasonHistory {
	
	public static final int SIZE = 0x3348;
	
	public long fileOffset;
	public int year;
	public int numTracks = 0;
	public int numDrivers;
	public int numTeams;
	public ArrayList<Track> tracks;
	public ArrayList<Driver> drivers;
	public ArrayList<Team> teams;
	
	public SeasonHistory() {
		tracks = new ArrayList<>();
		drivers = new ArrayList<>();
		teams = new ArrayList<>();
	}
	
	public String toString() {
		return "" + year;
	}
	
	public static SeasonHistory createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var t = new SeasonHistory();
		t.fileOffset = offset;
		t.year = input.readInt(offset);
		t.numTracks = input.readInt(offset + 4);
		for (int i = 0; i < t.numTracks; i++) {
			t.tracks.add(Track.createFrom(input, offset + 8 + i * Track.SIZE));
		}
		t.numDrivers = input.readInt(offset + 0xc40);
		for (int i = 0; i < t.numDrivers; i++) {
			t.drivers.add(Driver.createFrom(input, offset + 0xc44 + i * Driver.SIZE));
		}
		t.numTeams = input.readInt(offset + 0x2e04);
		input.position(old);
		return t;
	}
}
