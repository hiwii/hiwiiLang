package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class ExpressSentence extends Expression {
	private String type;
	private Expression left;
	private Expression right;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}
}
