package net.hiwii.entity;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * 
 * @author Administrator
 * 一个表达式，用于包裹entity。
 *
 */
public class EntityWrapper extends Expression {
	private Entity content;
	public Entity getContent() {
		return content;
	}
	public void setContent(Entity content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "a(" + content.toString() + ")";
	}
}
