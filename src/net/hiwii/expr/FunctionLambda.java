package net.hiwii.expr;

import java.util.List;

import net.hiwii.view.Entity;

public class FunctionLambda extends FunctionExpression {
	private List<Entity> values;
	
	public List<Entity> getValues() {
		return values;
	}
	public void setValues(List<Entity> values) {
		this.values = values;
	}
}
