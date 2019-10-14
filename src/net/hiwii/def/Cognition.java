package net.hiwii.def;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;


public class Cognition extends Entity{
	private Expression left;
	private Entity value;
	public Expression getLeft() {
		return left;
	}
	public void setLeft(Expression left) {
		this.left = left;
	}
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
