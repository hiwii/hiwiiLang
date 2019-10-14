package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class UnaryOperation extends Expression {
	private String operator;
	private Expression operand;
	public UnaryOperation(String operator, Expression operand){
		this.operator = operator;
		this.operand = operand;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Expression getOperand() {
		return operand;
	}
	public void setOperand(Expression operand) {
		this.operand = operand;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return operator + operand.toString();
	}

}
