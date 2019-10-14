package net.hiwii.lambda;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * x=>expression  //single argument
 * or
 * (x,y)=>expression //more arguments
 * @author a
 *
 */
public class LambdaFunction extends Expression{
	private List<String> keys;
	private Expression statement;
	public List<String> getKeys() {
		return keys;
	}
	public void setKeys(List<String> keys) {
		this.keys = keys;
	}
	public Expression getStatement() {
		return statement;
	}
	public void setStatement(Expression statement) {
		this.statement = statement;
	}
	
	/**
	 * 最后返回格式是：
	 * (arg0,arg1...)执行表达式
	 * 重新转换为表达式需要单独解析
	 */
	@Override
	public String toString() {
		if(keys.size() == 1){
			return keys.get(0) + "=>" + statement.toString();
		}
		String ret = "(";
		int i = 0;
		for(String name:keys){
			if(i == 0) {
				ret = ret + name;
			}else {
				ret = ret + name + ",";
			}
			i++;
		}

		ret = ret + ")=>" + statement.toString();
		return ret;
	}
}
