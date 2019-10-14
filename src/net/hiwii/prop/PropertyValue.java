package net.hiwii.prop;

import net.hiwii.view.Entity;

public class PropertyValue extends Entity {
	private Property prop;
	private Entity value;
	
	public Property getProp() {
		return prop;
	}
	public void setProp(Property prop) {
		this.prop = prop;
	}
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
