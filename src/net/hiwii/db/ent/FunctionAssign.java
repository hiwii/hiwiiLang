package net.hiwii.db.ent;

import java.util.List;

import net.hiwii.view.Entity;

public class FunctionAssign {
	private String name;
	private Entity value;
	private List<Entity> arguments;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
	public List<Entity> getArguments() {
		return arguments;
	}
	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
	
}
