package net.hiwii.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sleepycat.je.DatabaseException;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.context.HiwiiContext;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Assignment;
import net.hiwii.def.Declaration;
import net.hiwii.def.Definition;
import net.hiwii.def.decl.FunctionDeclaration;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.SubjectOperation;
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
	
	private List<Entity> entities;  //single entities
//	private NavigableMap<String,List<Entity>> entityList;  //entity list
	
	private NavigableMap<String,Assignment> assignments;
	private NavigableMap<String,JudgmentResult> judgments;
	private NavigableMap<String,String> switches; //state array
	
	private NavigableMap<String,Expression> calculs;
	private NavigableMap<String,Expression> actions;
	private NavigableMap<String,Expression> decisions;
	
	//exist or not exist
	private boolean positive;
	
	public HiwiiInstance() {
		persisted = false;
		entities = new ArrayList<Entity>();
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

		
	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
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
		if(name.equals("add")) {
			if(args.size() != 1) {
				return new HiwiiException();
			}
			Entity ent = args.get(0);
//			Definition def;
//			try {
//				def = EntityUtil.proxyGetDefinition(ent.getClassName());
//			} catch (Exception e) {
//				return new HiwiiException();
//			}
//			if(def == null) {
//				return new HiwiiException();
//			}
			entities.add(ent);
			return persistChild(ent);
		}
		return null;
	}
	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionDecision(name, args);
	}
	
	public Expression doMappingAction(String name, List<Expression> args){
		if(name.equals("declare")) {
			if(args.size() != 2) {
				return new HiwiiException();
			}
			return doDeclare(args.get(0), args.get(1));
		}
		return null;
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
//			String key = entities.floorKey(sign);
//			if(key != null){
//				
//			}
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

	public Expression doDeclare(Expression source, Expression expr) {
		if(source instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) source;
			if(!(bo.getOperator().equals(":"))){
				return new HiwiiException();
			}
			
			Expression left = bo.getLeft();
			String cogn = null;

			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				cogn = ie.getName();
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();
			if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
				char tp = 0;
				if(cogn.equals("Action")){
					tp = 'a';
				}else if(cogn.equals("Calculation")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return doDeclare(tp, right, expr);
			}
		}else {
			return new HiwiiException();
		}
		return new NormalEnd();		
	}
	
	public Expression doDeclare(char type, Expression source, Expression expr){
		String name = null;

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(source instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) source;
				name = ie.getName();
				if(type == 'c'){
					db.putInstIdCalculation(this, name, expr.toString(), null);
				}else if(type == 'd'){

				}else{
					db.putIdAction(name, expr.toString(), null);
				}
			}else if(source instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) source;
				
				if(type == 'c'){
					db.putFunctionCalculation(fe, expr, null);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{
//					db.putFunAction(fd, null);
				}
			}else if(source instanceof MappingExpression){

			}else if(source instanceof SubjectOperation){
				SubjectOperation so = (SubjectOperation) source;
				if(!(so.getSubject() instanceof IdentifierExpression)){
					//不允许有修饰出现,someObject do f1(), another do f2().那么有不同的definition
					return new HiwiiException();
				}
				IdentifierExpression id = (IdentifierExpression) so.getSubject();
				Definition def = EntityUtil.proxyGetDefinition(id.getName());
				if(def != null){
					if(so.getAction() instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) so.getAction();
						Declaration dec = new Declaration();
						dec.setName(ie.getName());
						dec.setStatement(expr);
						if(type == 'c'){
							db.putIdCalculation(name, expr.toString(), null);
						}else if(type == 'd'){

						}else{

						}
					}else if(so.getAction() instanceof FunctionExpression){
						FunctionExpression fe = (FunctionExpression) so.getAction();
						name = fe.getName();
						FunctionDeclaration fd = new FunctionDeclaration();
//						fd.setName(name);
						try {
							List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//							fd.setArguments(args);
						} catch (ApplicationException e) {
							return new HiwiiException();//参数错误
						}

						fd.setStatement(expr);
//						if(type == 'c'){
//							db.putFunCalculation(fd, null);
//						}else if(type == 'd'){
//							db.putFunDecision(fd, null);
//						}else{
//							db.putFunAction(fd, null);
//						}
					}
				}
				
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		} 

		return new NormalEnd();
	}
	
	public Expression setProperty(String name, Entity value){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Assignment ass = assignments.get(name);
		try {
			if(ass == null){
				ass = new Assignment();
				ass.setValue(value);
				ass.setName(name);
//				ass.setProp(prop);
				db.putIdAssignment(uuid, name, value, null);
				this.getAssignments().put(ass.getName(), ass);
				return new NormalEnd();
			}else{
				ass.setValue(value);
				db.putIdAssignment(uuid, name, value, null);				
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
	
	public Expression persistChild(Entity child) {
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putChildEntity(uuid, child, null);
			return new NormalEnd();
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
		
		str = str  + "{";
		int last = assignments.size() - 1;
		int i = 0;
		if(!entities.isEmpty()) {
			str = str + "[";
			for(Entity ent:entities){
				if(i == 0) {	
					str = str + ent.toString();
				}else {
					str = str + "," + ent.toString();
				}
				i++;
			}
			str = str + "]";
		}
		if(assignments.size() == 0 && judgments.size() == 0){
			return str;
		}
		i = 0;
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
