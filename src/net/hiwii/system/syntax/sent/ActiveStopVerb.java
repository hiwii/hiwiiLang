package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class ActiveStopVerb extends Expression{
	private Expression active;
	private Expression verb;
	public Expression getActive() {
		return active;
	}
	public void setActive(Expression subject) {
		this.active = subject;
	}
	public Expression getVerb() {
		return verb;
	}
	public void setVerb(Expression action) {
		this.verb = action;
	}
}
