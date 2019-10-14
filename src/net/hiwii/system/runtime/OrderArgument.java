package net.hiwii.system.runtime;

import net.hiwii.cognition.Expression;

public class OrderArgument extends Expression{
	private Expression cognition;
	private boolean ascent;
	public Expression getCognition() {
		return cognition;
	}
	public void setCognition(Expression cognition) {
		this.cognition = cognition;
	}
	public boolean isAscent() {
		return ascent;
	}
	public void setAscent(boolean ascent) {
		this.ascent = ascent;
	}

}
