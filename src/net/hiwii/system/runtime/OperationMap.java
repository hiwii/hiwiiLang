package net.hiwii.system.runtime;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 
 * @author ha-wangzhenhai
 * option:expression,block,function...
 * argument:List<Expression>
 * argument option:literal,type + name, name
 * ���ڲ������壬�������ֲ������������ַ���runtime��
 * type name��ʽ�ı��������У�name���ܺ��ⲿ�Ѷ��������ͬ������������������塣
 * java�г���������ͻʱ��ͨ��this.name��name�������֡�
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
