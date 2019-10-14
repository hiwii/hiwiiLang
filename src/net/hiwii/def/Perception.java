package net.hiwii.def;

import net.hiwii.view.Entity;

/**
 * Perception·ÖÎªmemory/persistence
 * @author ha-wangzhenhai
 *
 */
public class Perception extends Entity {
	private Definition type;
	private String signature;//#number

	public String getSignature() {
		return signature;
	}
	public void setSignature(String id) {
		this.signature = id;
	}
	public Definition getType() {
		return type;
	}

	public void setType(Definition type) {
		this.type = type;
	}


	@Override
	public String getClassName() {
		return type.getName();
	}

	@Override
	public String toString() {
		return signature;
	}
	
}
