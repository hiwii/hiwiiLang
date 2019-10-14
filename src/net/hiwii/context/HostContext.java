package net.hiwii.context;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

public class HostContext extends RuntimeContext {
	private Entity host;

	public HostContext(Entity host) {
		this.host = host;
	}

	public Entity getHost() {
		return host;
	}

	public void setHost(Entity host) {
		this.host = host;
	}
	
	public Expression doAction(Expression expr){
		return host.doAction(expr);
	}
	
	public Entity doCalculation(Expression expr){
		return host.doCalculation(expr);
	}
}
