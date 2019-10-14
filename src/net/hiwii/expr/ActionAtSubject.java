package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class ActionAtSubject extends Expression {
	private Expression subject;
	private Expression action;
	public Expression getSubject() {
		return subject;
	}
	public void setSubject(Expression subject) {
		this.subject = subject;
	}
	public Expression getAction() {
		return action;
	}
	public void setAction(Expression action) {
		this.action = action;
	}
}
