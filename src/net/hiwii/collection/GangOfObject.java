package net.hiwii.collection;

import net.hiwii.def.Abstraction;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.view.Entity;

public class GangOfObject extends Entity {
	private RealNumber number;
	private Abstraction target;
	public RealNumber getNumber() {
		return number;
	}
	public void setNumber(RealNumber number) {
		this.number = number;
	}
	public Abstraction getTarget() {
		return target;
	}
	public void setTarget(Abstraction target) {
		this.target = target;
	}
}
