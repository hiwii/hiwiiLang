package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class PluralExpression extends Expression {
	private String name;
	public PluralExpression(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
