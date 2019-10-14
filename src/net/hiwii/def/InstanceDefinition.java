package net.hiwii.def;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.hiwii.cognition.Expression;
import net.hiwii.part.EntityPart;
import net.hiwii.prop.Property;
import net.hiwii.view.HiwiiInstance;

public class InstanceDefinition extends Definition {
//	private String name;
//	private String parent;
//	private String signature;
	private NavigableMap<String,EntityPart> parts;
	private NavigableMap<String,Property> props;
	private List<String> states;
	private List<Expression> characteristics;
	
	private HiwiiInstance instance;
	//instanceπ≤Õ¨ Ù–‘
	public InstanceDefinition() {
		props = new TreeMap<String,Property>();
//		defines = new TreeMap<String,Definition>();
		states = new ArrayList<String>();
//		setClosing(false);
	}
	
	public HiwiiInstance getInstance() {
		return instance;
	}

	public void setInstance(HiwiiInstance instance) {
		this.instance = instance;
	}

	public NavigableMap<String, EntityPart> getParts() {
		return parts;
	}

	public void setParts(NavigableMap<String, EntityPart> parts) {
		this.parts = parts;
	}

	public NavigableMap<String, Property> getProps() {
		return props;
	}

	public void setProps(NavigableMap<String, Property> props) {
		this.props = props;
	}
	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public List<Expression> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(List<Expression> characteristics) {
		this.characteristics = characteristics;
	}

	@Override
	public String getClassName() {
		return getName();
	}
	

}
