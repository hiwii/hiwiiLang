package net.hiwii.lambda;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * expression@var1
 * or
 * expression@(var1,var2)
 * @author a
 *
 */
public class MappingApplication extends Expression {
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
	 * ��󷵻ظ�ʽ�ǣ�
	 * (arg0,arg1...)ִ�б��ʽ
	 * ����ת��Ϊ���ʽ��Ҫ��������
	 */
	@Override
	public String toString() {
		if(keys.size() == 1){
			return keys.get(0) + "=>" + statement.toString();
		}
		String ret = "[";
		int i = 0;
		for(String name:keys){
			if(i == 0) {
				ret = ret + name;
			}else {
				ret = ret + name + ",";
			}
			i++;
		}

		ret = ret + "]=>" + statement.toString();
		return ret;
	}

}
