package net.hiwii.expr;

import net.hiwii.expr.sent.SubjectAction;

public class SubjectOperation extends SubjectAction {
//	private Expression subject;
//	private Expression target;
//	public Expression getSubject() {
//		return subject;
//	}
//	public void setSubject(Expression subject) {
//		this.subject = subject;
//	}
//	public Expression getTarget() {
//		return target;
//	}
//	public void setTarget(Expression target) {
//		this.target = target;
//	}
	@Override
	public String toString() {
		String str = this.getSubject().toString();
		str = str + "." + this.getAction().toString();
		return str;
	}
}
