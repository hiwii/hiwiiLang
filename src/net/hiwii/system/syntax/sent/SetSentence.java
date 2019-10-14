package net.hiwii.system.syntax.sent;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.AssignmentExpression;

public class SetSentence extends Expression {
	private List<String> names;
	private List<AssignmentExpression> assigns;
	public List<String> getNames() {
		return names;
	}
	public void setNames(List<String> names) {
		this.names = names;
	}
	public List<AssignmentExpression> getAssigns() {
		return assigns;
	}
	public void setAssigns(List<AssignmentExpression> assigns) {
		this.assigns = assigns;
	}
}
