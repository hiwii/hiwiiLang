package net.hiwii.entity;

import net.hiwii.view.Entity;

/**
 * a(definition)�������ճ��е�һ������Ӣ���е� a rabbit
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
