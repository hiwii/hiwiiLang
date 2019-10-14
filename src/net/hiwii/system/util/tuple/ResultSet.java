package net.hiwii.system.util.tuple;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.view.Entity;

public class ResultSet extends Entity {
	private List<Entity> values;

	public ResultSet() {
		values = new ArrayList<Entity>();
	}

	public List<Entity> getValues() {
		return values;
	}

	public void setValues(List<Entity> values) {
		this.values = values;
	}
	
	public void putValue(Entity value){
		values.add(value);
	}
	
	public Entity getValue(){
		if(values.size() == 1){
			return values.get(0);
		}else{
			return null;
		}
	}
	
	public Entity getValue(int n){
		if(values.size() <= n && n > 0){
			return values.get(n + 1);
		}else{
			return null;
		}
	}
}
