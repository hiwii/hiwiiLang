package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.BraceExpression;

public class LoopSentence extends Expression {
	private boolean until;
	private Expression judge;
	private BraceExpression program;
	public boolean isUntil() {
		return until;
	}
	public void setUntil(boolean until) {
		this.until = until;
	}
	public Expression getJudge() {
		return judge;
	}
	public void setJudge(Expression judge) {
		this.judge = judge;
	}
	public BraceExpression getProgram() {
		return program;
	}
	public void setProgram(BraceExpression program) {
		this.program = program;
	}

}
