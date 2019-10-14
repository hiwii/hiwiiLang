package net.hiwii.lambda;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

public class EntityLambda extends Expression {
	private List<String> keys;
	private Expression statement;
	private List<Entity> arguments;
	
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

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
