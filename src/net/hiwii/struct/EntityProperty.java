package net.hiwii.struct;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.def.Definition;
import net.hiwii.prop.Property;
import net.hiwii.view.Entity;

public class EntityProperty extends Property {
	private Definition type;
	private List<Expression> limits;
	private String name;
	private Entity value;
	
	public void setType(Definition type) {
		this.type = type;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
