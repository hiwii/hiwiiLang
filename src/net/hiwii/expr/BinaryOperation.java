package net.hiwii.expr;

import net.hiwii.cognition.Expression;
import net.hiwii.context.RuntimeLadder;
import net.hiwii.entity.EntityWrapper;
import net.hiwii.system.util.ExpressionUtil;
import net.hiwii.view.Entity;

public class BinaryOperation extends Expression {
	private String operator;
	private Expression left;
	private Expression right;
	public BinaryOperation(){
		
	}
	public BinaryOperation(String operator, Expression left, Expression right){
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
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

//	@Override
//	public Expression getEnhancedArgument(List<Expression> args) {
//		BinaryOperation bo = new BinaryOperation();
//		bo.setOperator(operator);
//		Expression le = left.getEnhancedArgument(args);
//		if(le instanceof HiwiiException){
//			return left;
//		}
//		Expression re = right.getEnhancedArgument(args);
//		if(re instanceof HiwiiException){
//			return right;
//		}
//		bo.setLeft(le);
//		bo.setRight(re);
//		return bo;
//	}
	
//	@Override
//	public Expression calculateExpression(RuntimeLadder ladder) {
//		if(!ExpressionUtil.isLiteral(left)){
//			Entity result = ladder.getChains().get(0).doContextCalculation(left, null);
//			EntityWrapper ew = ExpressionUtil.wrap(result);
//			
//		}
//		return null;
//	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return left.toString() + operator + right.toString();
	}

}
