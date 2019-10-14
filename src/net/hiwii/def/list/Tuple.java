package net.hiwii.def.list;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.NullValue;
import net.hiwii.collection.TupleValue;
import net.hiwii.def.Definition;
import net.hiwii.message.HiwiiException;
import net.hiwii.view.Entity;

public class Tuple extends Definition {
	private List<Argument> names;
	
	public Tuple() {
		setSignature("L.T");
	}
	public Tuple(List<String> names) {
		setSignature("L.I");
	}
	public List<Argument> getNames() {
		return names;
	}
	public void setNames(List<Argument> names) {
		this.names = names;
	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("new")){
			if(names == null || names.size() < 2){
				return new HiwiiException();
			}
			TupleValue list = new TupleValue();
			List<Entity> items = new ArrayList<Entity>();
			int pos = 0;
			int len = names.size();
			while(pos < len){
				items.add(new NullValue());
				pos++;
			}
			list.setItems(items);
			list.setType(this);
			return list;
		}
		return null;
	}
}
