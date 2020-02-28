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
import net.hiwii.context.HiwiiContext;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Assignment;
import net.hiwii.def.Definition;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.prop.Property;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;

/**
 * preAction/postAction
 * onState
 * entities、property和judgment构成object状态。
 * 面向对象中property构成状态。
 * 定义和对象是不同的。property和status用于定义，是抽象的，
 * 而propertyValue和judgment用于对象，是具体的。
 * @author Administrator
 *
 */
public class HiwiiInstance extends Entity {
//	private Definition inner;  //inner definition
//	private String type;   //each entity belong to a definition.
	private boolean persisted;
	private String uuid;
	private String name;
	private String master;
	private boolean closing;
	//property must be defined in definition
	
	private NavigableMap<String,Entity> entities;  //single entities
	private NavigableMap<String,List<Entity>> entityList;  //entity list
	
	private NavigableMap<String,Assignment> assignments;
	private NavigableMap<String,JudgmentResult> judgments;
	private NavigableMap<String,String> switches; //state array
	
	private NavigableMap<String,Expression> calculs;
	private NavigableMap<String,Expression> actions;
	private NavigableMap<String,Expression> decisions;
	
	//exist or not exist
	private boolean positive;
	
	public HiwiiInstance() {
		entities = new TreeMap<String,Entity>();
		assignments = new TreeMap<String,Assignment>();
		judgments = new TreeMap<String,JudgmentResult>();
		switches = new TreeMap<String,String>();
	}
	
	public boolean isPersisted() {
		return persisted;
	}

	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public boolean isClosing() {
		return closing;
	}

	public void setClosing(boolean closing) {
		this.closing = closing;
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
	public NavigableMap<String, String> getSwitches() {
		return switches;
	}

	public void setSwitches(NavigableMap<String, String> switches) {
		this.switches = switches;
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
	public Entity doMappingCalculation(String name, List<Expression> args, HiwiiContext context) {
		
		return null;
	}

	@Override
	public Expression doMappingAction(String name, List<Expression> args, HiwiiContext context){
		if(name.equals("assign")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			Entity value = doCalculation(args.get(1));
			if(value instanceof HiwiiException){
				return (Expression) value;
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			String prop = ie.getName();
			return setProperty(prop, value);
		}else if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			Expression jdg = doDecision(args.get(1), context);
			if(!(jdg instanceof JudgmentResult)) {
				return jdg;
			}
			JudgmentResult value = (JudgmentResult) jdg;
			if(judgments.containsKey(name)){
				JudgmentResult now = judgments.get(name);
				if(EntityUtil.judge(now) != EntityUtil.judge(value)) {
					return confirm(name, value);
				}			
			}else{
				judgments.put(name, value);
				return confirm(name, value);
			}
		}
		return null;
	}
	
	@Override
	public Expression doAssign(String name, Entity value) {
		if(assignments.containsKey(name)){
			return setProperty(name, value);
		}else{
			Definition def;
			try {
				def = EntityUtil.proxyGetDefinition(name);
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
			if(def == null){
				return new HiwiiException();
			}
			String sign = def.getSignature();
			String key = entities.floorKey(sign);
			if(key != null){
				
			}
		}
		return new NormalEnd();
	}
	
	@Override
	public Expression doJudge(String name, JudgmentResult value) {
		if(judgments.containsKey(name)){
			JudgmentResult now = judgments.get(name);
			if(EntityUtil.judge(now) != EntityUtil.judge(value)) {
				return confirm(name, value);
			}			
		}else{
			judgments.put(name, value);
			return confirm(name, value);
		}
		return new NormalEnd();
	}
	
	public Expression doSwitch(String name, Expression value) {
		return null;
	}

	public Expression setProperty(String name, Entity value){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Assignment ass = assignments.get(name);
		try {
			if(ass == null){
				ass = new Assignment();
				String pkey = name + "@" + uuid;
				Property prop = EntityUtil.proxyGetProperty(name);//, this.getClassName());
						//takeFromEntity(name, this);
				if(prop == null){
					return new HiwiiException();
				}
				if(!EntityUtil.judgeValueToProperty(value, prop)){
					return new HiwiiException();
				}
				ass.setValue(value);
				ass.setName(name);
//				ass.setProp(prop);
				db.putAssignment(pkey, ass, null);
				this.getAssignments().put(ass.getName(), ass);
				return new NormalEnd();
			}else{
				Property prop = EntityUtil.proxyGetProperty(name, this.getClassName());
				if(!EntityUtil.judgeValueToProperty(value, prop)){
					return new HiwiiException();
				}
				String pkey = name + "@" + uuid;
				ass.setValue(value);
				db.putAssignment(pkey, ass, null);				
				return new NormalEnd();
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression confirm(String name, JudgmentResult value){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(judgments.containsKey(name)){
				String pkey = name + "@" + uuid;
				
				db.turnJudgment(pkey, value, null);
				return new NormalEnd();
			}else{
				String pkey = name + "@" + uuid;

				db.putJudgment(pkey, value, null);
				return new NormalEnd();
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
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
		boolean coma = true;
		if(i == 0){ //assignments.size() == 0
			head = "";
			coma = false;
		}
		if(judgments.size() > 0) {
			str = str + head;
		}
		
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
		
		head = ",";
		if(!coma){ //assignments.size() == 0
			head = "";
		}
		if(switches.size() > 0) {
			str = str + head;
		}
		i = 0; //reinitialize i
		last = switches.size() - 1;
		for(Entry<String, String> entry:switches.entrySet()){
			String tail = ",";
			if(i == last){
				tail = "";
			}
			String output = entry.getKey() + ":" + entry.getValue();
			
			str = str + output + tail;
			i++;
		}
		str = str + "}\r\n";
		return str;
	}
	
}
