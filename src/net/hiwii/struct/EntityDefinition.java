package net.hiwii.struct;

import java.util.List;

import net.hiwii.def.Definition;
import net.hiwii.view.Entity;

public class EntityDefinition extends Entity {
	private List<Entity> values;
	private Definition type;//²¿¼þ
	private boolean countable;
	private int count;
	public List<Entity> getValues() {
		return values;
	}
	public void setValues(List<Entity> values) {
		this.values = values;
	}
	public Definition getType() {
		return type;
	}
	public void setType(Definition type) {
		this.type = type;
	}
	public boolean isCountable() {
		return countable;
	}
	public void setCountable(boolean countable) {
		this.countable = countable;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
