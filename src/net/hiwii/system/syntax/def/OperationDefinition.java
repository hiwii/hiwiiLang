package net.hiwii.system.syntax.def;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 
 * @author ha-wangzhenhai
 * operation definition format:
 * name(expression0 | type0 arg0, expression0 | type0 arg0...)
 * 参数定义可以是任意类型、常量、指定类型的变量
 * 当type是任意对象，则type可以省略
 * expression必须以[]括住
 * identifier对象视同expression。
 *
 */
public class OperationDefinition extends Expression {
	private String name;
	private List<Expression> args;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Expression> getArgs() {
		return args;
	}
	public void setArgs(List<Expression> args) {
		this.args = args;
	}

}
