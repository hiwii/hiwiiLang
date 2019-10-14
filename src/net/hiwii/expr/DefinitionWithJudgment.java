package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class DefinitionWithJudgment extends Expression {
	private String name;
	private Expression judgement;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getJudgement() {
		return judgement;
	}
	public void setJudgement(Expression judgement) {
		this.judgement = judgement;
	}
}
