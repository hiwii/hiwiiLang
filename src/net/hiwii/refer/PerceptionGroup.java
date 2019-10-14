package net.hiwii.refer;

import java.util.List;

import net.hiwii.view.Entity;

public class PerceptionGroup extends PerceptionManager {

	@Override
	public Entity doIdentifierCalculation(String name) {
//		if(name.equals("count")){
//			return new IntegerNumber(String.valueOf(context.getPerceptions().size()));
//		}
		return null;
	}

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionCalculation(name, args);
	}
	
}
