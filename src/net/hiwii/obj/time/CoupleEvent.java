package net.hiwii.obj.time;

import net.hiwii.view.Entity;

public class CoupleEvent extends Entity {
	private HiwiiEvent when;//state time
	private HiwiiAction action;
	public HiwiiEvent getWhen() {
		return when;
	}
	public void setWhen(HiwiiEvent when) {
		this.when = when;
	}
	public HiwiiAction getAction() {
		return action;
	}
	public void setAction(HiwiiAction action) {
		this.action = action;
	}
}
