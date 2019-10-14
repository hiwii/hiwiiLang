package net.hiwii.system.syntax.def;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * types首先作为索引条件
 * 建立type索引的目的是，当name声明相同时，如何快速确定引用执行部分。
 * type索引只取主定义部分，以进行初步的判断。
 * compound主定义是compound name
 * synthesis主定义是synthesis name
 * name.decorate---主定义是name
 * name@host----主定义是name
 * 当前不考虑这个问题，因此
 * 函数名相同，参数个数相同情况下，需要进行每个比对
 * @author ha-wangzhenhai
 *
 */
public class DeclaredExpression extends Expression {
	private List<String> arguments;//参数名
	private List<Expression> types;
	private List<Expression> limits;//参数条件
	private Expression condition;
	private Expression statement;
	
	public List<String> getArguments() {
		return arguments;
	}
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
	public List<Expression> getTypes() {
		return types;
	}
	public void setTypes(List<Expression> types) {
		this.types = types;
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
	public Expression getStatement() {
		return statement;
	}
	public void setStatement(Expression statement) {
		this.statement = statement;
	}
}
