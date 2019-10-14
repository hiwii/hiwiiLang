package net.hiwii.lambda;

import java.util.List;

import net.hiwii.cognition.Expression;

public class ArgumentedLambda extends Expression {
	private List<String> keys;
	private Expression statement;
	private List<Expression> arguments;
	
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
	public List<Expression> getArguments() {
		return arguments;
	}

	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public String toString() {
		String ret = "TODO";
		return ret;
	}
}
