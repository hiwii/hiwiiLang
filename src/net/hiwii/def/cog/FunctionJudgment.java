package net.hiwii.def.cog;

import java.util.List;

import net.hiwii.def.Judgment;
import net.hiwii.view.Entity;

public class FunctionJudgment extends Judgment {
	private List<Entity> arguments;

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
