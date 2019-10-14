package net.hiwii.expr.sent;

import java.util.List;

import net.hiwii.cognition.Expression;

public class ConditionExpression extends Expression{
	private Expression body;
	private List<Expression> conditions;
	public Expression getBody() {
		return body;
	}
	public void setBody(Expression body) {
		this.body = body;
	}
	public List<Expression> getConditions() {
		return conditions;
	}
	public void setConditions(List<Expression> conditions) {
		this.conditions = conditions;
	}
	@Override
	public String toString() {
		String str = body.toString();
		str = str + "{";
		if(conditions.size() >= 1){
			str = str + conditions.get(0).toString();
		}
		for(Expression exp:conditions.subList(1, conditions.size())){
			str = "," + exp.toString();
		}
		str = str + "}";
		return str;
	}
	
}
