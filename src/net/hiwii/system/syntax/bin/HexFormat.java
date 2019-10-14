package net.hiwii.system.syntax.bin;


public class HexFormat extends BinaryExpression {
	private String value;
	
	public HexFormat(String value) {
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
