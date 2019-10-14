package net.hiwii.system.store;

/**
 * 用于记录free cell start和size
 * @author Administrator
 *
 */
public class CellPointer {
	private int pointer;
	private int size;
	private CellPointer parent;
//	private int child;
	public int getPointer() {
		return pointer;
	}
	public void setPointer(int pos) {
		this.pointer = pos;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public CellPointer getParent() {
		return parent;
	}
	public void setParent(CellPointer parent) {
		this.parent = parent;
	}

}
