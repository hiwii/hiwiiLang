package net.hiwii.collection;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.view.Entity;

public class EntityMapping extends Entity {
	private Map<String, Entity> values;
	
	public EntityMapping() {
		super();
		values = new HashMap<String, Entity>();
	}

	public Map<String, Entity> getValues() {
		return values;
	}

	public void setValues(Map<String, Entity> values) {
		this.values = values;
	}

	public void put(String key, Entity value) {
		if(values == null) {
			values = new HashMap<String, Entity>();
		}
		values.put(key, value);
	}
	@Override
	public String toString() {
		String ret = "{";
		int i = values.size();
		for(Map.Entry<String, Entity> ent:values.entrySet()){
			if(i > 1) {
				ret = ent.getKey() + ":" + ent.getValue().toString() + ",";
			}else {
				ret = ent.getKey() + ":" + ent.getValue().toString();
			}
		}
		
		ret = ret + "}";
		return ret;
	}
}
