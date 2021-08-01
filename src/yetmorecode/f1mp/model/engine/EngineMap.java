package yetmorecode.f1mp.model.engine;

import java.io.IOException;
import java.util.ArrayList;

import yetmorecode.file.BinaryFileInputStream;

public class EngineMap {
	public static final int SIZE = 400;
	
	public long fileOffset;
	public ArrayList<Integer> values;
	
	public EngineMap() {
		values = new ArrayList<>();
	}
	
	public static EngineMap createFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var em = new EngineMap();
		em.fileOffset = offset;
		for (int i = 0; i < 100; i++) {
			var v = input.readInt(offset + i*4);
			if (v > 0) {
				em.values.add(v);	
			}
		}
		input.position(old);
		return em;
	}
}
