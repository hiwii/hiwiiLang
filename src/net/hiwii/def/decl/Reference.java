package net.hiwii.def.decl;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * refer/reRefer
 * 
 * refer(type:id/function.limit(condition))
 * type:=identifier:a(type)|some(type)|array(type).new[]
 * type=definition:definition.decorate()
 * @author Administrator
 *
 */
public class Reference extends Entity {
	private String name;
	private Expression type; //type
	private Expression limit;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getType() {
		return type;
	}
	public void setType(Expression type) {
		this.type = type;
	}
	public Expression getLimit() {
		return limit;
	}
	public void setLimit(Expression limit) {
		this.limit = limit;
	}

}
