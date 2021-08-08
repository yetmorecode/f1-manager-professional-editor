package yetmorecode.f1mp.model.track;

import java.io.IOException;

import yetmorecode.f1mp.model.DateModel;
import yetmorecode.f1mp.model.results.PodiumResult;
import yetmorecode.file.BinaryFileInputStream;

public class TrackInfo {
	public String name;
	public int region;
	public DateModel date;
	public int spriteOffset;
	public int unknown1;
	public int laps;
	public int movie;
	public int lapLength;
	public int raceLength;
	public int unknown2;
	public int unknown3;
	public String country;
	public PodiumResult podium;
	public String feature1;
	public String feature2;
	public String feature3;
	public String feature4;
	public int unknown4;
	public int unknown5;
	public int unknown6;
	public int unknown7;
	
	public static TrackInfo createFrom(BinaryFileInputStream input) throws IOException {
		var t = new TrackInfo();
		t.name = input.readString(24).trim();
		t.region = input.readInt();
		t.date = DateModel.readFrom(input);
		t.spriteOffset = input.readInt();
		t.unknown1 = input.readInt();
		t.laps = input.readInt();
		t.movie = input.readInt();
		t.lapLength = input.readInt();
		t.raceLength = input.readInt();
		t.unknown2 = input.readInt();
		t.unknown3 = input.readInt();
		t.country = input.readString(40).trim();
		t.podium = PodiumResult.createFrom(input);
		t.feature1 = input.readString(60).trim();
		t.feature2 = input.readString(60).trim();
		t.feature3 = input.readString(60).trim();
		t.feature4 = input.readString(60).trim();
		t.unknown4 = input.readInt();
		t.unknown5 = input.readInt();
		t.unknown6 = input.readInt();
		t.unknown7 = input.readInt();
		return t;
	}
	
	public static TrackInfo createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var t = createFrom(input);
		input.position(old);
		return t;
	}
}
