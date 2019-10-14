package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.BraceExpression;

public class ForLoopSentence extends Expression {
	private Expression init;
	private Expression condition;
	private Expression post;
	private BraceExpression program;
	
	public Expression getInit() {
		return init;
	}
	public void setInit(Expression init) {
		this.init = init;
	}
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
	public Expression getPost() {
		return post;
	}
	public void setPost(Expression post) {
		this.post = post;
	}
	public BraceExpression getProgram() {
		return program;
	}
	public void setProgram(BraceExpression program) {
		this.program = program;
	}
}
