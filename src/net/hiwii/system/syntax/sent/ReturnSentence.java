package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

/**
 * 该语句只用于函数运算中。在action中return与break等作为keyword解释
 * @author ha-wangzhenhai
 *
 */
public class ReturnSentence extends Expression {
	private Expression result;

	public Expression getResult() {
		return result;
	}

	public void setResult(Expression result) {
		this.result = result;
	}
}
