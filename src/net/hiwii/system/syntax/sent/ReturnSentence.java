package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

/**
 * �����ֻ���ں��������С���action��return��break����Ϊkeyword����
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
