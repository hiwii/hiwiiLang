package net.hiwii.cognition.result;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

public class ReturnResult extends Expression{
	private Entity result;

	public Entity getResult() {
		return result;
	}

	public void setResult(Entity result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return result.toString();
	}
}
