package net.hiwii.expr.sent;

import net.hiwii.cognition.Expression;

public class SubjectAction extends Expression {
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
	
	@Override
	public String toString() {
		String str = this.getSubject().toString();
		str = str + "#" + this.getAction().toString();
		return str;
	}
}
