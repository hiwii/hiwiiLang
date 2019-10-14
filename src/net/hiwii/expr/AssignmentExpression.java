package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class AssignmentExpression extends Expression {
	private String name;
	private Expression expression;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}
