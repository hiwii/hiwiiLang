package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class PluralDescription extends Expression {
	private String name;
	private List<AssignmentExpression> assigns;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AssignmentExpression> getAssigns() {
		return assigns;
	}
	public void setAssigns(List<AssignmentExpression> assigns) {
		this.assigns = assigns;
	}
}
