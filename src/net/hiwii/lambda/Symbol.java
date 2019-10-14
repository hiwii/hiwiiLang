package net.hiwii.lambda;

import net.hiwii.cognition.Expression;

public class Symbol extends Expression {
	private String value;

	public Symbol(String value) {
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
