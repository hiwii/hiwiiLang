package net.hiwii.system.info;

import java.util.List;

import net.hiwii.view.Entity;

public class FunctionPropertyInfo extends Entity {
	private List<Entity> args;
	private Entity value;
	public List<Entity> getArgs() {
		return args;
	}
	public void setArgs(List<Entity> args) {
		this.args = args;
	}
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
