package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class IdentifySentence extends Expression {
	private String name;
	private Expression target;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getTarget() {
		return target;
	}
	public void setTarget(Expression target) {
		this.target = target;
	}
}
