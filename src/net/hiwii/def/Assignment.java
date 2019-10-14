package net.hiwii.def;

import net.hiwii.view.Entity;

/**
 * from:hosted Object
 * @author Administrator
 *
 */
public class Assignment extends Entity {
	private String name;
	private Entity value;
//	private Property prop;

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
}
