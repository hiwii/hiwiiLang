package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class RangeExpression extends Expression {
	private boolean leftIn;
	private boolean rightIn;
	private Expression left;
	private Expression right;
	public boolean isLeftIn() {
		return leftIn;
	}
	public void setLeftIn(boolean leftIn) {
		this.leftIn = leftIn;
	}
	public boolean isRightIn() {
		return rightIn;
	}
	public void setRightIn(boolean rightIn) {
		this.rightIn = rightIn;
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
