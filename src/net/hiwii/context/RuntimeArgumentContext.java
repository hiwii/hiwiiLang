package net.hiwii.context;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * 在以函数形式调用program的情况下使用。
 * @author ha-wangzhenhai
 *
 */
public class RuntimeArgumentContext extends RuntimeContext{
	private NavigableMap<String,Entity> arguments;
	
	public RuntimeArgumentContext() {
		arguments = new TreeMap<String,Entity>();
	}
	public NavigableMap<String, Entity> getArguments() {
		return arguments;
	}
	public void setArguments(NavigableMap<String, Entity> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(arguments.containsKey(name)){
			return arguments.get(name);
		}
		return super.doIdentifierCalculation(name);
	}
	
//	@Override
//	public Entity doIdentifierCalculation(Entity subject, String name) {
//		if(arguments.containsKey(name)){
//			return arguments.get(name);
//		}
//		return super.doIdentifierCalculation(subject, name);
//	}
	
	public Expression putArgument(String name, Entity value){//throws ApplicationException?处理重复
//		if(arguments.containsKey(name)){
//			return new HiwiiException();
//		}
		arguments.put(name, value);
		return null;
	}
	
}
