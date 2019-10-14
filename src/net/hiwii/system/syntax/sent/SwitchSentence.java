package net.hiwii.system.syntax.sent;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.BraceExpression;

public class SwitchSentence extends Expression {
	private Expression formula;
	private List<BraceExpression> results;//Program只是List形式，代替List<List<Expression>>
	private List<Expression> programs;
	private Expression doElse;
	public Expression getFormula() {
		return formula;
	}
	public void setFormula(Expression formula) {
		this.formula = formula;
	}
	
	public List<BraceExpression> getResults() {
		return results;
	}
	public void setResults(List<BraceExpression> results) {
		this.results = results;
	}
	public List<Expression> getPrograms() {
		return programs;
	}
	public void setPrograms(List<Expression> programs) {
		this.programs = programs;
	}
	public Expression getDoElse() {
		return doElse;
	}
	public void setDoElse(Expression doElse) {
		this.doElse = doElse;
	}
}
