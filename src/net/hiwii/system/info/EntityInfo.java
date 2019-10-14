package net.hiwii.system.info;

import java.util.NavigableMap;
import java.util.TreeMap;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

public class EntityInfo extends Entity {
	private String id;
	private NavigableMap<String,Expression> props;
	
	private NavigableMap<String,Entity> assignId;
	/*
	 * function_name+arguments Ë÷Òý
	 */
	private NavigableMap<String,Entity> assignFunc;
	
	private NavigableMap<String,Expression> actions;
	private NavigableMap<String,Expression> status;
	
	public EntityInfo() {
		props = new TreeMap<String,Expression>();
		assignId = new TreeMap<String,Entity>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public NavigableMap<String, Expression> getActions() {
		return actions;
	}
	public void setActions(NavigableMap<String, Expression> actions) {
		this.actions = actions;
	}
	public NavigableMap<String, Expression> getStatus() {
		return status;
	}
	public void setStatus(NavigableMap<String, Expression> status) {
		this.status = status;
	}
	
	public NavigableMap<String, Expression> getProps() {
		return props;
	}
	public void setProps(NavigableMap<String, Expression> props) {
		this.props = props;
	}
	public NavigableMap<String, Entity> getAssignId() {
		return assignId;
	}
	public void setAssignId(NavigableMap<String, Entity> assignId) {
		this.assignId = assignId;
	}
	public NavigableMap<String, Entity> getAssignFunc() {
		return assignFunc;
	}
	public void setAssignFunc(NavigableMap<String, Entity> assignFunc) {
		this.assignFunc = assignFunc;
	}
	
	
	public boolean containsProp(String name){
		return props.containsKey(name);
	}
	
	public void putPropertyInfo(String name, Expression limit){
		props.put(name, limit);
	}
	
	public boolean containsAssignId(String name){
		return assignId.containsKey(name);
	}
	
	public void putAssignId(String name, Entity value){
		assignId.put(name, value);
	}
}
