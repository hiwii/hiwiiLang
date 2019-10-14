package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class NegateOperation extends Expression {
	private Expression target;
	public NegateOperation() {
		
	}
	public NegateOperation(Expression target) {
		this.target = target;
	}
	public Expression getTarget() {
		return target;
	}
	public void setTarget(Expression target) {
		this.target = target;
	}
}
