package yetmorecode.f1mp.model;

import java.io.IOException;

import yetmorecode.file.BinaryFileInputStream;
import yetmorecode.file.BinaryFileOutputStream;

public class DateModel {

	public static final int SIZE = 20;
	
	public int hour;
	public int day;
	public int month;
	public int year;
	public int millis;
	
	public static DateModel readFrom(BinaryFileInputStream input) throws IOException {
		var d = new DateModel();
		d.hour = input.readInt();
		d.day = input.readInt();
		d.month = input.readInt();
		d.year = input.readInt();
		d.millis = input.readInt();
		return d;
	}
	
	public static DateModel readFrom(BinaryFileInputStream input, long offset) throws IOException {
		var old = input.position(offset);
		var d = readFrom(input);
		input.position(old);
		return d;
	}
	
	public static DateModel writeTo(BinaryFileOutputStream output, long offset, DateModel d) throws IOException {
		//var old = output.position(offset);
		
		//output.position(old);
		return d;
	}
}
