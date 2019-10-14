package net.hiwii.cognition.flow;

import net.hiwii.cognition.Expression;
import net.hiwii.message.HiwiiException;

public class ExceptionReturn extends Expression {
	private HiwiiException result;

	public HiwiiException getResult() {
		return result;
	}

	public void setResult(HiwiiException result) {
		this.result = result;
	}
}
