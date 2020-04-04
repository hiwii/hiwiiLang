package net.hiwii.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.collection.EnumSet;
import net.hiwii.def.Definition;
import net.hiwii.def.SimpleDefinition;
import net.hiwii.def.list.Array;
import net.hiwii.def.list.ListClass;
import net.hiwii.def.list.SetClass;
import net.hiwii.def.list.Tuple;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.util.StringUtil;
import net.hiwii.user.GroupDefinition;
import net.hiwii.user.UserDefinition;
import net.hiwii.view.Entity;

/**
 * Localhost是一个特殊对象。所以定义并不是localhost的子定义。
 * 所以定义都可以认为是localhost的组成部分。
 * 20160520before
 * 系统定义的几个最上层已定义
 * level 0:L for LocalHost
 * level 1:[O/C] Object/Cognition
 * level Cognition: [A|E|L|S] Abstraction|Expression|List|Set
 * 
 * @author ha-wangzhenhai
 *
 */
public class SystemDefinition {
	public static Map<String,Definition> defs =
		new HashMap<String,Definition>();
	public static Map<String,String> idname =
			new HashMap<String,String>();
	static{
		defs.put("LocalHost", new SimpleDefinition("LocalHost","L"));
		defs.put("Object", new SimpleDefinition("Object","L.O"));//继承Operation
		defs.put("Cognition", new SimpleDefinition("Cognition","L.C"));//继承Operation
		
		defs.put("List", new ListClass());
		defs.put("Set", new SetClass());
		defs.put("Array", new Array());
		defs.put("Tuple", new Tuple());
		defs.put("Expression", new SimpleDefinition("Expression","L.O.E"));
		
		defs.put("Action", new SimpleDefinition("Action","L.A"));
		defs.put("Judgment", new SimpleDefinition("Judgment","L.J"));//status
		
		defs.put("Time", new SimpleDefinition("Time","L.O.t"));
		
		defs.put("year", new SimpleDefinition("year","L.O.y"));
		defs.put("month", new SimpleDefinition("month","L.O.m"));
		defs.put("day", new SimpleDefinition("day","L.O.d"));
		defs.put("hour", new SimpleDefinition("hour","L.O.h"));
		defs.put("minute", new SimpleDefinition("minute","L.O.mi"));
		defs.put("second", new SimpleDefinition("second","L.O.s"));
		
		defs.put("Round", new SimpleDefinition("Round","L.C.R"));
		
		defs.put("String", new SimpleDefinition("String","L.O.E.S"));    //"L.C.E.S"
		defs.put("Number", new SimpleDefinition("Number","L.O.E.N"));  //"L.C.E.N"
//		defs.put("Character", "L.C.E.C"); character is regular expression,using "c" instead of 'c'
		
		defs.put("Integer", new SimpleDefinition("Integer","L.O.E.N.I"));
		defs.put("Float", new SimpleDefinition("Float","L.O.E.N.F"));
		
		defs.put("User", new UserDefinition());
		defs.put("Group", new GroupDefinition());
		defs.put("Role", new SimpleDefinition("Role","Role"));
		
//		defacts.put("L.C.Se|enum", "");
		idname.put("L", "LocalHost");
		idname.put("L.O", "Object");//继承Operation
		idname.put("L.C", "Cognition");//继承Operation
		
		idname.put("L.A", "Action");
		idname.put("L.J", "Judgment");//status
		
		idname.put("L.O.t", "Time");
		
		idname.put("L.O.y", "year");
		idname.put("L.O.m", "month");
		idname.put("L.O.d", "day");
		idname.put("L.O.h", "hour");
		idname.put("L.O.mi", "minute");
		idname.put("L.O.s", "second");
		
		idname.put("L.C.L", "List");
		idname.put("L.C.S", "Set");
		idname.put("L.C.E", "Expression");
		
//		idname.put("Round", new SimpleDefinition("LocalHost","L.C.R"));
		
		idname.put("L.O.E.S", "String");
		idname.put("L.O.E.N", "Number");
//		idname.put("Character", "L.C.E.C"); character is regular expression,using "c" instead of 'c'
		
		idname.put("L.C.E.N.I", "Integer");
		idname.put("L.C.E.N.F", "Float");
		
		idname.put("User", "User");
		idname.put("Group", "Group");
		idname.put("Role", "Role");

	}
	
	
	public static boolean contains(String name){
		return defs.containsKey(name);
	}
	
	
	/**
	 * sig这里指定义名
	 * @param sig
	 * @param name
	 * @param args
	 * @return
	 */
	public static Entity doFunctionOperation(String sig, String name, List<Entity> args){
		if(sig.equals("Set")){
			if(name.equals("enum")){
				//EntityUtil.hasDuplicate(args)确保没有重复 if(yes) exception
				EnumSet es = new EnumSet();
				es.setItems(args);
				return es;
			}
		}
		return null;
	}
	
	public static Expression doIsPositive(String name1, String name2){
		String sig1,sig2;
		if(defs.containsKey(name1)){
			sig1 = defs.get(name1).getSignature();
		}else{
			return new HiwiiException();
		}
		if(defs.containsKey(name2)){
			sig2 = defs.get(name2).getSignature();
		}else{
			return new HiwiiException();
		}
		JudgmentResult jr = new JudgmentResult();
		if(StringUtil.matched(sig1, sig2)){
			jr.setResult(true);			
		}else{
			jr.setResult(false);	
		}
		return jr;
	}
}
