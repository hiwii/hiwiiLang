package net.hiwii.collection;

import net.hiwii.def.Abstraction;

public class ObjectContainer extends Abstraction {
	private Abstraction container;
	private Abstraction target;
	public Abstraction getContainer() {
		return container;
	}
	public void setContainer(Abstraction container) {
		this.container = container;
	}
	public Abstraction getTarget() {
		return target;
	}
	public void setTarget(Abstraction target) {
		this.target = target;
	}
}
