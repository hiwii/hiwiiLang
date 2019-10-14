package net.hiwii.system.syntax.bin;

import net.hiwii.cognition.Expression;

public class BinaryValue extends Expression {
	private byte[] value;

	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	@Override
	public String toString() {
		String ret = "";
		for(byte b:value){
//			ret = ret + (new Integer(byte));
		}
		return super.toString();
	}
}
