package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class WhileSentence extends Expression {
	private Expression judge;
	private Expression statement;
	public Expression getJudge() {
		return judge;
	}
	public void setJudge(Expression judge) {
		this.judge = judge;
	}
	public Expression getStatement() {
		return statement;
	}
	public void setStatement(Expression statement) {
		this.statement = statement;
	}
}
