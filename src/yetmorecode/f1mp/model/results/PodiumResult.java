package yetmorecode.f1mp.model.results;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;

public class PodiumResult {
	public String first;
	public String firstShort;
	public String second;
	public String secondShort;
	public String third;
	public String thirdShort;
	
	public static PodiumResult readFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var r = new PodiumResult();
		r.first = input.readString(40).trim();
		r.firstShort = input.readString(4).trim();
		r.second = input.readString(40).trim();
		r.secondShort = input.readString(4).trim();
		r.third = input.readString(40).trim();
		r.thirdShort = input.readString(4).trim();
		input.position(old);
		return r;
	}
}
