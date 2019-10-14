package net.hiwii.system.syntax.def;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * types������Ϊ��������
 * ����type������Ŀ���ǣ���name������ͬʱ����ο���ȷ������ִ�в��֡�
 * type����ֻȡ�����岿�֣��Խ��г������жϡ�
 * compound��������compound name
 * synthesis��������synthesis name
 * name.decorate---��������name
 * name@host----��������name
 * ��ǰ������������⣬���
 * ��������ͬ������������ͬ����£���Ҫ����ÿ���ȶ�
 * @author ha-wangzhenhai
 *
 */
public class DeclaredExpression extends Expression {
	private List<String> arguments;//������
	private List<Expression> types;
	private List<Expression> limits;//��������
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
