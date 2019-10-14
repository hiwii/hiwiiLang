package net.hiwii.system.info;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * 用于记录Property信息
 * from记录host信息，可以是定义，也可以是对象
 * limit记录属性的类型和修饰信息，可以是simpleDefinition、decoratedDefinition...
 * @author ha-wangzhenhai
 *
 */
public class PropertyInfo extends Expression {
	private Entity from;
	private Expression limit;
	
	public Entity getFrom() {
		return from;
	}
	public void setFrom(Entity from) {
		this.from = from;
	}
	public Expression getLimit() {
		return limit;
	}
	public void setLimit(Expression limit) {
		this.limit = limit;
	}
}
