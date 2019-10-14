package net.hiwii.system.runtime;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 
 * @author ha-wangzhenhai
 * option:expression,block,function...
 * argument:List<Expression>
 * argument option:literal,type + name, name
 * 对于参数定义，常量部分不处理，变量部分放入runtime。
 * type name格式的变量定义中，name不能和外部已定义变量相同，否则会引起引用歧义。
 * java中出现命名冲突时，通过this.name和name进行区分。
 * 
 * format:[var1 pos1,var2 pos2...](Expression)
 *
 */
public class OperationMap{
	private List<VariablePosition> args;
	private Expression expression;
	
	public List<VariablePosition> getArgs() {
		return args;
	}
	public void setArgs(List<VariablePosition> args) {
		this.args = args;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = "[";
		int i = 1;
		for(VariablePosition vp:args){
			ret = ret + vp.getName()+ " " + vp.getPosition();
			if(i < args.size()){
				ret = ret + ",";
			}
			i++;
		}
		ret = ret + "]";
		ret = ret + "(";
		ret = ret + expression.toString() + ")";
		return ret;
	}
}
