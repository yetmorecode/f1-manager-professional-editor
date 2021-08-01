package yetmorecode.f1mp.model.track;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class Track {
	public String name;
	public int segmentsTotal;
	public int spriteOffset;
	public String cameraName;
	public float unknown1;
	public int number;
	public float unknown2;
	public int region;
	
	public int sprite1;
	public int sprite2;
	public int spriteStandings;
	
	public int pitEntry;
	public int pitExit;
	public int pitLimiterEntry;
	public int pitLimiterExit;
	
	public int fuel;
	public int weather;
	
	public int index;
	
	
	public long fileOffset;
	public long fileSize = 0x26350;
	
	public ArrayList<TrackSegment> segments;
	public ArrayList<TrackPoint> points;
	public ArrayList<Integer> garages;
	public ArrayList<TrackCamera> cameras;
	
	public Track() {
		segments = new ArrayList<>();
		points = new ArrayList<>();
		garages = new ArrayList<>();
		cameras = new ArrayList<>();
	}
	
	public static Track createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var t = new Track();
		t.name = input.readString(60);
		t.cameraName = input.readString(offset + 0x3c, 12);
		t.unknown1 = input.readFloat(offset + 0x48);
		t.spriteOffset = input.readInt(offset + 0x114);
		t.segmentsTotal = input.readInt(offset + 0x11c);
		t.number = input.readInt(offset + 0x26308);
		t.unknown2 = input.readFloat(offset + 0x120);
		t.sprite1 = input.readInt(offset + 0x114);
		t.sprite2 = input.readInt(offset + 0x118);
		t.spriteStandings = input.readInt(offset + 0x26340);
		t.fileOffset = offset;
		t.pitEntry = input.readInt(offset + 0x26204);
		t.pitExit = input.readInt(offset + 0x26208);
		t.pitLimiterEntry = input.readInt(offset + 0x26348);
		t.pitLimiterExit = input.readInt(offset + 0x2634c);
		t.region = input.readInt(offset + 0x26344);
		t.fuel = input.readInt(offset + 0x110);
		t.index = input.readInt(offset + 0x128);
		t.weather = input.readInt(offset + 0x2633c);
		
		for (int i = 0; i < t.segmentsTotal; i++) {
			t.segments.add(TrackSegment.createFrom(input, offset + 0x140 + i*20));
		}
		
		for (int i = 0; i < (t.pitExit - t.pitEntry + t.segmentsTotal) % t.segmentsTotal; i++) {
			t.points.add(TrackPoint.createFrom(input, offset + 0x22420 + i * 12));
		}
		for (int i = 0; i < 12; i++) {
			var g = input.readInt(offset + 0x2630c + i*4);
			t.garages.add(g);
		}
		for (int i = 0; i < 30; i++) {
			t.cameras.add(TrackCamera.createFrom(input, offset + 0x26214 + i*8));
		}
		
		input.position(old);
		return t;
	}
	
	public String getRegionName() {
		switch(region) {
		case 1:
			return "Europe";
		case 2:
			return "Asia";
		case 3:
			return "America";
		case 4:
			return "Australia";
		}
		return "unknown";
	}
	
	public String toString() {
		return String.format("%s", name);
	}
}
