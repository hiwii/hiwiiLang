package net.hiwii.def.decl;

import java.util.List;

import net.hiwii.prop.Property;
import net.hiwii.view.Entity;

public class FunctionProperty extends Property {
	private List<Entity> arguments;

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
