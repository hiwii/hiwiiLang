package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;


public class ListExpression extends Expression {
	private String type;
	private List<Expression> arguments;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Expression> getArguments() {
		return arguments;
	}
	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}

}
