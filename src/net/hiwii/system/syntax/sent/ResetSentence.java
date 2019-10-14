package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;


public class ResetSentence extends Expression {
	private String type;
	private String name;
	private Expression expression;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
