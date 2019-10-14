package net.hiwii.entity;

import net.hiwii.view.Entity;

/**
 * a(definition)函数，日常中的一个对象，英语中的 a rabbit
 * @author Administrator
 *
 */
public class SingleEntity extends Entity {
	private Entity target;

	public Entity getTarget() {
		return target;
	}
	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "a(" + target.toString() + ")";
	}
}
