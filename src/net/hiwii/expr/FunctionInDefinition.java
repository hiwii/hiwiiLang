package net.hiwii.expr;

import net.hiwii.system.syntax.cog.AssignSentence;

public class FunctionInDefinition extends AssignSentence {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
