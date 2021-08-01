package yetmorecode.f1mp.model.rsc;

/**
 * Represents one resource as found in f1_e.rsc file
 * 
 * This can be either:
 *  - a vga color palette
 *  - a PCX fullscreen image
 *  - a sprite (with multiple images)
 *  - a game sound (todo)
 *
 * Each resource has an offset in the *.rsc file. Optionally each resource
 * can have a name or an color palette offset (for PCX and sprites).
 */
public class ResourceDefinition {

	public enum Type {
		TYPE_PALETTE,
		TYPE_PCX,
		TYPE_SPRITE,
		TYPE_SOUND
	}
	
	private long offset;
	private long paletteOffset;
	private String name;
	private Type type;
	private long size;
	public long[] references;
	
	public ResourceDefinition(long offset, Type type) {
		setOffset(offset);
		setType(type);
	}
	
	public ResourceDefinition(long offset, String name, Type type) {
		this(offset, type);
		setName(name);
	}
	
	public ResourceDefinition(long offset, String name, Type type, long paletteOffset) {
		this(offset, name, type);
		setPaletteOffset(paletteOffset);
	}
	
	public ResourceDefinition(long offset, String name, Type type, long paletteOffset, long[] refs) {
		this(offset, name, type, paletteOffset);
		this.references = refs;
	}
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getOffset() {
		return offset;
	}
	
	
	public void setOffset(long offset) {
		this.offset = offset;
	}
	
	public String getName() {
		return name != null ? name : Long.toString(offset, 16);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public long getPaletteOffset() {
		return paletteOffset;
	}
	
	public void setPaletteOffset(long paletteOffset) {
		this.paletteOffset = paletteOffset;
	}
	
	public String toString() {
		return String.format("%s (%x)", getName(), getOffset());
	}
}
