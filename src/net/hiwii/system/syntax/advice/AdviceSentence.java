package net.hiwii.system.syntax.advice;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.BraceExpression;

/**
 * 
 * @author ha-wangzhenhai
 *
 */
public class AdviceSentence extends Expression {
	private String type;//begin/end action(operation judgment perceive)
	private Expression statement;
	private BraceExpression program;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BraceExpression getScript() {
		return program;
	}
	public void setScript(BraceExpression program) {
		this.program = program;
	}
}
