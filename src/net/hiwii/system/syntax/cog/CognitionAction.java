package net.hiwii.system.syntax.cog;

import net.hiwii.cognition.Expression;

public class CognitionAction extends Expression {
	boolean persist;
	private Expression base;
	public boolean isPersist() {
		return persist;
	}
	public void setPersist(boolean persist) {
		this.persist = persist;
	}
	public Expression getBase() {
		return base;
	}
	public void setBase(Expression base) {
		this.base = base;
	}
}
