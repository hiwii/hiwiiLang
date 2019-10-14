package net.hiwii.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.def.TypeView;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class Collection extends Entity {
	private List<Entity> items;

	public Collection(){
		items = new ArrayList<Entity>();
	}
	public Collection(List<Entity> items){
		this.items = items;
	}
	public List<Entity> getItems() {
		return items;
	}

	public void setItems(List<Entity> items) {
		this.items = items;
	}
	
	public boolean typeDecision(TypeView tv) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		for(Entity ent:items){
			boolean jdg = tv.doAccept(ent);
			if(!jdg){
				return false;
			}
		}
		return true;
	}
}
