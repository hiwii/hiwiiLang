package net.hiwii.refer;

import java.util.List;

import net.hiwii.context.HiwiiContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.def.Definition;
import net.hiwii.view.Entity;

public class PerceptionManager extends Refer {
	private RuntimeContext context;
	private Definition define;
	public PerceptionManager() {
		
	}
	public PerceptionManager(RuntimeContext context) {
		this.context = context;
	}

	public HiwiiContext getContext() {
		return context;
	}

	public void setContext(RuntimeContext context) {
		this.context = context;
	}

	public Definition getDefine() {
		return define;
	}

	public void setDefine(Definition define) {
		this.define = define;
	}

	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("count")){
			return null;//new IntegerNumber(String.valueOf(context.getPerceptions().size()));
		}else if(name.equals("just")){
			return context.getJust();
		}
//		if(name.equals("last")){
//			String key = define.getSignature();
//			String last = context.getPerceptions().floorKey(key + "0");
//			if(StringUtil.matched(last, key) && !StringUtil.matched(last, key + "0")){
//				return context.getPerceptions().get(last);
//			}else{
//				return new NullExpression();
//			}
//		}else if(name.equals("first")){
//			String key = define.getSignature();
//			String last = context.getPerceptions().ceilingKey(key);
//			if(StringUtil.matched(last, key + "#")){
//				return context.getPerceptions().get(last);
//			}else{
//				return new NullExpression();
//			}
//		}
		return null;
	}

	/**
	 * count,sum,avg
	 */
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		// TODO Auto-generated method stub
		return super.doFunctionCalculation(name, args);
	}

	@Override
	public String toString() {
		return "Perception";
	}
}
