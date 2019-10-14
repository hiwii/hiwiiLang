package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class MappingLambda extends MappingExpression {
	private List<Expression> values;

	public List<Expression> getValues() {
		return values;
	}
	public void setValues(List<Expression> values) {
		this.values = values;
	}
}
