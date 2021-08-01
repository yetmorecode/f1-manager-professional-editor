package yetmorecode.f1mp.model.engine;

import java.io.IOException;
import java.util.HashMap;

import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.format.lx.LxExecutable;

public class EngineResources {
	public static long PCX_ENGINE_SUPPLIER_LOGOS = 0;
	public static HashMap<Integer, Integer> engineToLogo = new HashMap<>();
	
	public static void createFrom(BinaryFileInputStream input, LxExecutable exe) throws IOException {
		var old = input.position(0);
		
		PCX_ENGINE_SUPPLIER_LOGOS = input.readInt(exe.getOffsetInFile(0, 0x170010, 0x230719+1));

		var offset = exe.getOffsetInFile(2, 0x2c59e0, 0x2d4a14+4);
		for (int i = 0; i < 10; i++) {
			var index = input.readInt(offset + i*4);
			engineToLogo.put(i, index);
		}
		
		input.position(old);
	}
		
}
