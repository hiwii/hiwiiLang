package net.hiwii.def;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.SystemDefinition;
import net.hiwii.view.Entity;

public class SimpleDefinition extends Definition{
	public SimpleDefinition() {
		
	}
	public SimpleDefinition(String name) {
		setName(name);
	}
	
	public SimpleDefinition(String name, String sign) {
		setName(name);
		setSignature(sign);
	}

	@Override
	public String toString() {
		return getName();
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
//		if(name.equals("enum")){
//			//EntityUtil.hasDuplicate(args)确保没有重复 if(yes) exception
//			EnumSet es = new EnumSet();
//			es.setItems(args);
//			return es;
//		}
//		return new HiwiiException();
		return SystemDefinition.doFunctionOperation(getName(), name, args);
	}
	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionAction(name, args);
	}
	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		if(name.equals("is")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(ent instanceof Abstraction){
				if(ent instanceof SimpleDefinition){
					SimpleDefinition sd = (SimpleDefinition) ent;
					return SystemDefinition.doIsPositive(getName(), sd.getName());
				}
			}else{
				return new HiwiiException();
			}
		}
		return super.doFunctionDecision(name, args);
	}
	
	public Expression doIsPositive(Entity target){
		if(target instanceof Abstraction){
			if(target instanceof SimpleDefinition){
				SimpleDefinition sd = (SimpleDefinition) target;
				return SystemDefinition.doIsPositive(getName(), sd.getName());
			}else if(target instanceof DecoratedDefinition){
				return new HiwiiException();
			}else{
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}
	}
}
