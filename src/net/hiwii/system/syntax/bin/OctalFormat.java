package net.hiwii.system.syntax.bin;


public class OctalFormat extends BinaryExpression {
	private String value;
	
	public OctalFormat(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}
}
