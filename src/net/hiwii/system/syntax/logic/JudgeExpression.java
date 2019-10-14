package net.hiwii.system.syntax.logic;

import net.hiwii.cognition.Expression;

public class JudgeExpression extends Expression {
	private String operator;
	private Expression left;
	private Expression right;

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
