package net.hiwii.expr;

import net.hiwii.expr.sent.SubjectAction;

public class SubjectStatus extends SubjectAction {
//	private Expression subject;
//	private Expression target;
	private boolean right;
	
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
	public boolean isRight() {
		return right;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
}
