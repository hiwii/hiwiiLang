package net.hiwii.view;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.def.Assignment;
import net.hiwii.def.Definition;
import net.hiwii.message.HiwiiException;
import net.hiwii.prop.Property;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;

public class VarInstance extends Entity {
	private String name;
	//property must be defined in definition
	
	private NavigableMap<String,Entity> entities;  //single entities
	private NavigableMap<String,List<Entity>> entityList;  //entity list
	
	private NavigableMap<String,Assignment> assignments;
	private NavigableMap<String,JudgmentResult> judgments;
	
	private NavigableMap<String,Expression> calculs;
	private NavigableMap<String,Expression> actions;
	private NavigableMap<String,Expression> decisions;
	
	//exist or not exist
	private boolean positive;
	
	public VarInstance() {
		entities = new TreeMap<String,Entity>();
		assignments = new TreeMap<String,Assignment>();
		judgments = new TreeMap<String,JudgmentResult>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NavigableMap<String, Entity> getEntities() {
		return entities;
	}
	public void setEntities(NavigableMap<String, Entity> entities) {
		this.entities = entities;
	}
	public NavigableMap<String, List<Entity>> getEntityList() {
		return entityList;
	}
	public void setEntityList(NavigableMap<String, List<Entity>> entityList) {
		this.entityList = entityList;
	}
	public NavigableMap<String, Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(NavigableMap<String, Assignment> assignments) {
		this.assignments = assignments;
	}
	public NavigableMap<String, JudgmentResult> getJudgments() {
		return judgments;
	}
	public void setJudgments(NavigableMap<String, JudgmentResult> judgments) {
		this.judgments = judgments;
	}
	public NavigableMap<String, Expression> getCalculs() {
		return calculs;
	}
	public void setCalculs(NavigableMap<String, Expression> calculs) {
		this.calculs = calculs;
	}
	public NavigableMap<String, Expression> getActions() {
		return actions;
	}
	public void setActions(NavigableMap<String, Expression> actions) {
		this.actions = actions;
	}
	public NavigableMap<String, Expression> getDecisions() {
		return decisions;
	}
	public void setDecisions(NavigableMap<String, Expression> decisions) {
		this.decisions = decisions;
	}
	/**
	 * 是否真实存在，还是虚幻(not exist)。
	 * @return
	 */
	public boolean isPositive() {
		return positive;
	}
	public void setPositive(boolean positive) {
		this.positive = positive;
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(assignments.containsKey(name)){
			Assignment ass = assignments.get(name);
			return ass.getValue();
		}
		return null;
	}
	@Override
	public Expression doIdentifierAction(String name) {
		// TODO Auto-generated method stub
		return super.doIdentifierAction(name);
	}
	@Override
	public Expression doIdentifierDecision(String name) {
		// TODO Auto-generated method stub
		return super.doIdentifierDecision(name);
	}
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionCalculation(name, args);
	}
	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
//		if(name.equals(anObject))
		return super.doFunctionAction(name, args);
	}
	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionDecision(name, args);
	}
	
	@Override
	public Expression doAssign(String name, Entity value) {
		try {
			Property prop = EntityUtil.proxyGetProperty(name, getClassName());
			if(prop != null){
				if(assignments.containsKey(name)){
					Assignment ass = assignments.get(name);
					ass.setValue(value);
				}else{
					Assignment ass = new Assignment();
					ass.setName(name);
					ass.setValue(value);
					assignments.put(name, ass);
				}
			}else{
				Definition def;				
				def = EntityUtil.proxyGetDefinition(name);
				
				if(def == null){
					return new HiwiiException();
				}
				String sign = def.getSignature();
				String key = entities.floorKey(sign);
				if(key != null){
					
				}
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
		return new NormalEnd();
	}
	
	public Entity getPropertyValue(String name){
		if(assignments.containsKey(name)){
			Assignment ass = assignments.get(name);
			return ass.getValue();
		}
		return new NullValue();
	}
	
	@Override
	public String toString() {
		String str = getClassName();
		if(name != null){
			str = str + ":" + name;
		}
		if(assignments.size() == 0 && judgments.size() == 0){
			return str;
		}
		str = str  + "{";
		int last = assignments.size() - 1;
		int i = 0;
		for(Assignment ass:assignments.values()){
			String tail = ",";
			if(i == last){
				tail = "";
			}
			String output = "";
			if(ass.getValue() instanceof HiwiiInstance){
				HiwiiInstance inst = (HiwiiInstance) ass.getValue();
				output = inst.getName();
			}else{
				output = ass.getValue().toString();
			}
			str = str + ass.getName() + ":" + output + tail;
			i++;
		}
		String head = ",";
		if(i == 0){ //assignments.size() == 0
			head = "";
		}
		str = str + head;
		i = 0; //reinitialize i
		last = judgments.size() - 1;
		for(Entry<String, JudgmentResult> entry:judgments.entrySet()){
			String tail = ",";
			if(i == last){
				tail = "";
			}
			String output = entry.getKey() + ":" + entry.getValue().toString();
			
			str = str + output + tail;
			i++;
		}
		str = str + "}\r\n";
		return str;
	}
	

}
