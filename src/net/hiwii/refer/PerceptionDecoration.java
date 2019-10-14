package net.hiwii.refer;

import net.hiwii.cognition.Expression;

public class PerceptionDecoration extends PerceptionGroup {
	private Expression condition;
	
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}
}
