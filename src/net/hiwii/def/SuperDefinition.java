package net.hiwii.def;

import java.util.NavigableMap;

import net.hiwii.prop.Property;

public class SuperDefinition extends Definition {
//	private Definition parent;
	private NavigableMap<String,Property> propDefine;
//	private List<String> states;

//	public Definition getParent() {
//		return parent;
//	}
//	public void setParent(Definition parent) {
//		this.parent = parent;
//	}
	public NavigableMap<String, Property> getPropDefine() {
		return propDefine;
	}

	public void setPropDefine(NavigableMap<String, Property> propDefine) {
		this.propDefine = propDefine;
	}

//	public List<String> getStates() {
//		return states;
//	}
//
//	public void setStates(List<String> states) {
//		this.states = states;
//	}
}