package net.hiwii.refer;

import net.hiwii.def.Definition;

public class DefinitionRefer extends Refer {
	private String name;
	private Definition define;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Definition getDefine() {
		return define;
	}
	public void setDefine(Definition define) {
		this.define = define;
	}
}
