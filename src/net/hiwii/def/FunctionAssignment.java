package net.hiwii.def;

import java.util.List;

import net.hiwii.view.Entity;

public class FunctionAssignment extends Assignment {
	private List<Entity> arguments;

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
