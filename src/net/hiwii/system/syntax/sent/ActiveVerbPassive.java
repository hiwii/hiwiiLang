package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class ActiveVerbPassive extends Expression {
	private Expression active;
	private Expression verb;
	private Expression passive;
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
	public Expression getPassive() {
		return passive;
	}
	public void setPassive(Expression hiobject) {
		this.passive = hiobject;
	}

}
