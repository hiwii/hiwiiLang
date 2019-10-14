package net.hiwii.system.syntax.cog;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.AssignmentExpression;

public class AssignSentence extends Expression {
	private List<AssignmentExpression> assigns;
	public List<AssignmentExpression> getAssigns() {
		return assigns;
	}
	public void setAssigns(List<AssignmentExpression> assigns) {
		this.assigns = assigns;
	}
}
