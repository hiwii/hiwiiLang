package net.hiwii.entity;

import net.hiwii.view.Entity;

public class ArrayItem extends Entity {
	private String name;
	private int [] arguments;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getArguments() {
		return arguments;
	}
	public void setArguments(int[] arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public String toString() {
		String ret = name + "[";
		int i = 0;
		for(int exp:arguments){
			if(i == 0)
				ret = ret + exp;
			else// if(i == size - 1)
				ret = ret + "," + exp;
		}
		ret = ret + "]";
		return ret;
	}
	
}
