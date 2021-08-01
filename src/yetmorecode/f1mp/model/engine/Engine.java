package yetmorecode.f1mp.model.engine;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class Engine {
	public static final long SIZE = 0xcfc;
	
	public boolean dirty = false;
	public int index;
	public long fileOffset;
	public String name;
	public String shortname;
	public int cost;
	public int length;
	public int width;
	public int height;
	public int weight;
	public int stars;
	public int hp;
	public String cylinders;
	public String valves;
	public String rpm;
	public String cubicCapacity;
	public ArrayList<EngineMap> maps;
	public int cooling;
	public int ignition1;
	public int ignition2;
	
	public Engine() {
		maps = new ArrayList<>();
	}
	
	public static Engine createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var e = new Engine();
		e.fileOffset = offset;
		e.name = new String(input.readNBytes(32)).trim();
		e.shortname = new String(input.readNBytes(20)).trim();
		e.cost = input.readInt();
		e.length = input.readInt();
		e.width = input.readInt();
		e.height = input.readInt();
		e.weight = input.readInt();
		e.hp = input.readInt();
		e.stars = input.readInt();
		e.cylinders = new String(input.readNBytes(4)).trim();
		e.valves = new String(input.readNBytes(4)).trim();
		e.rpm = new String(input.readNBytes(8)).trim();
		e.cubicCapacity = new String(input.readNBytes(8)).trim();
		input.position(offset + 0xcf0);
		e.cooling = input.readInt();
		e.ignition1 = input.readInt();
		e.ignition2 = input.readInt();
		for (int i = 0; i < 8; i++) {
			e.maps.add(EngineMap.createFrom(input, offset + 0x6c + i * EngineMap.SIZE));
		}
		input.position(old);
		return e;
	}
	
	public String toString() {
		return name;
	}
}
