package net.hiwii.system.info;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * ���ڼ�¼Property��Ϣ
 * from��¼host��Ϣ�������Ƕ��壬Ҳ�����Ƕ���
 * limit��¼���Ե����ͺ�������Ϣ��������simpleDefinition��decoratedDefinition...
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
