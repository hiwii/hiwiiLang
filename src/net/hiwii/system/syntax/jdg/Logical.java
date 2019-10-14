package net.hiwii.system.syntax.jdg;

import net.hiwii.cognition.Expression;

public class Logical extends Expression {
	private boolean positive;

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}
}
