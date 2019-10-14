package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class DoesStatement extends Expression {
	private String name;//name="does" to be drop
	private Expression argument;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getArgument() {
		return argument;
	}
	public void setArgument(Expression argument) {
		this.argument = argument;
	}
	@Override
	public String toString() {
		return argument.toString();
	}
}
