package net.hiwii.expr;

import net.hiwii.cognition.Expression;


public class ParenExpression extends Expression {
	private Expression expression;

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "(" + expression.toString() + ")";
	}
	
}
