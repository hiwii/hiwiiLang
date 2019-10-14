package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;


public class OperationExpression extends Expression{
	private List<Expression> array;

	public List<Expression> getArray() {
		return array;
	}

	public void setArray(List<Expression> array) {
		this.array = array;
	}

}
