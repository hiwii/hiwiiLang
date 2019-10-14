package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.message.HiwiiException;
import net.hiwii.view.Entity;


public class CharExpression extends Expression {
	private char value;

	public CharExpression(char value) {
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public String getClassName() {
		return "Charactor";
	}
	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		JudgmentResult jr = new JudgmentResult();
		if(name.equals("GT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 > op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}else if(name.equals("LT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 < op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}else if(name.equals("GE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 >= op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}else if(name.equals("LE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 <= op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}else if(name.equals("EQ")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 == op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}else if(name.equals("NE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof CharExpression){
				CharExpression in = (CharExpression) op;
				char op1=value;
				char op2 = in.getValue();
				if(op1 != op2){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}
		}
		return null;
	}
}
