package net.hiwii.expr;

import net.hiwii.cognition.Expression;

public class FilterExpression extends Expression {
	private Expression collection;
	private Expression logic;

	public Expression getCollection() {
		return collection;
	}

	public void setCollection(Expression collection) {
		this.collection = collection;
	}

	public Expression getLogic() {
		return logic;
	}

	public void setLogic(Expression logic) {
		this.logic = logic;
	}
}
