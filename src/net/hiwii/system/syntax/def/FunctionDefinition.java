package net.hiwii.system.syntax.def;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 函数声明后,声明的参数名、参数名定义域，参数条件定义保存在当前对象中
 * @author ha-wangzhenhai
 *
 */
public class FunctionDefinition extends Expression {
	private String name;
	private List<String> arguments;
	private List<Expression> limits;//参数条件
	private Expression condition;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getArguments() {
		return arguments;
	}
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	public Expression getCondition() {
		return condition;
	}
	public void setCondition(Expression condition) {
		this.condition = condition;
	}

}
