package net.hiwii.def;

import java.io.IOException;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class TypeView extends Entity {
	private String type;
	private List<Expression> limits;
	
	public TypeView() {
		
	}
	public TypeView(String type) {
		super();
		this.type = type;
	}
	public TypeView(String type, List<Expression> limits) {
		this.type = type;
		this.limits = limits;
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
	
	public boolean doAccept(Entity ent) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(EntityUtil.judgeDefinitionIsAnother(ent.getClassName(), type)){
			return true;
		}
		return false;
	}
}
