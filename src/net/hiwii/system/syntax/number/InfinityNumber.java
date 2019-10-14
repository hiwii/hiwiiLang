package net.hiwii.system.syntax.number;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class InfinityNumber extends NumberExpression {
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("plus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return new InfinityNumber();
		}else if(name.equals("minus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return new InfinityNumber();
		}else if(name.equals("multiply")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof NumberExpression){
				NumberExpression ne = (NumberExpression) op;
				if(ne.greatZero()){
					return new InfinityNumber();
				}else if(ne.greatZero()){
					return new NegativeInfinity();
				}else{
					return new HiwiiException();
				}
			}
		}else if(name.equals("divide")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof NumberExpression){
				NumberExpression ne = (NumberExpression) op;
				if(ne.greatZero()){
					return new InfinityNumber();
				}else if(ne.greatZero()){
					return new NegativeInfinity();
				}else{
					return new HiwiiException();
				}
			}
		}
		return null;
	}

	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		if(args.size() != 1){
			return new HiwiiException();
		}
		if(args.get(0) instanceof NumberExpression){
			return new HiwiiException();
		}
		
		NumberExpression ne = (NumberExpression) args.get(0);
		if(ne instanceof InfinityNumber){
			return new HiwiiException();
		}
		if(ne instanceof NegativeInfinity){
			return new HiwiiException();
		}
		if(name.equals("GT")){
			return EntityUtil.decide(true);
		}else if(name.equals("LT")){
			return EntityUtil.decide(false);
		}else if(name.equals("GE")){
			return EntityUtil.decide(true);
		}else if(name.equals("LE")){
			return EntityUtil.decide(false);
		}else if(name.equals("EQ")){
			return EntityUtil.decide(false);
		}else if(name.equals("NE")){
			return EntityUtil.decide(true);
		}
		return null;
	}

	@Override
	public boolean greatZero(){
		return true;
	}
	@Override
	public boolean lessZero(){
		return false;
	}
	@Override
	public boolean equalZero(){
		return false;
	}
}
