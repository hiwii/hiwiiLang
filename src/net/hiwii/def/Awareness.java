package net.hiwii.def;

import net.hiwii.view.Entity;

public class Awareness extends Entity {
	private String name;
	
	public Awareness(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getGene() {
		// TODO Auto-generated method stub
		return null;
	}

}
