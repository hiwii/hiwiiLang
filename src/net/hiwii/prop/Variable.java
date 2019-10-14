package net.hiwii.prop;

import net.hiwii.view.Entity;

/**
 * variable是property的系统属性。
 * property的定义和赋值分离。而variable统一在一起。
 * @author hiwii
 *
 */
public class Variable extends Property {
	private Entity value;
	
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
