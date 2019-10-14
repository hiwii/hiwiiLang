package net.hiwii.def.list;

import java.io.IOException;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.collection.EntityList;
import net.hiwii.def.Definition;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class ListClass extends Definition {
	private String type;
	private List<Expression> limits;
	
	public ListClass() {
//		super();
		setSignature("L.L");
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	
	public boolean judgeEntity(Entity value) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		boolean jdg = EntityUtil.judgeEntityIsDefinition(value, type, limits);
		return jdg;
	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("new")){
			EntityList list = new EntityList();
			list.setListType(this);
			return list;
		}
		return null;
	}
	@Override
	public String toString() {
		String ret = "List";
		return ret;
	}
}
