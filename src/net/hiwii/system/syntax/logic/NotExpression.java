package net.hiwii.system.syntax.logic;

import net.hiwii.cognition.Expression;

public class NotExpression extends Expression {
	private Expression expression;

	public NotExpression(Expression expression){
		this.expression = expression;
	}
	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

}
