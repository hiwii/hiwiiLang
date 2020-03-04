package net.hiwii.expr;

import java.io.ByteArrayInputStream;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.message.HiwiiException;
import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.SimpleRegular;
import net.hiwii.reg.atom.SystemPattern;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.SystemParser;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;


public class StringExpression extends SimpleRegular{
	private String value;
	
	public StringExpression() {
		super("");
		setClassName("String");
		this.value = "";
	}
	public StringExpression(String value) {
		super(value);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}



	/******************************************/
	@Override
	public String toString() {
//		return '\"' + value + '\"';
		return value;
	}
	
	/****************************/
	public Entity plus(StringExpression operand){
		return new StringExpression(value + operand.getValue());
	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("length")){
			String val= String.valueOf(value.length());
			return new IntegerNumber(val);
		}else if(name.equals("toExpression")){//Operation is Expression
			return toExpression();
		}else if(name.equals("reverse")){
			return new StringExpression(StringUtil.reverse(value));
		}else if(name.equals("upperCase")){
			return new StringExpression(value.toUpperCase());
		}else if(name.equals("lowerCase")){
			return new StringExpression(value.toLowerCase());
		}
		return null;
	}	
	

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("plus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof StringExpression){
				StringExpression se = (StringExpression) op;
				return new StringExpression(value + se.getValue());
			}
		}else if(name.equals("substring")){
			if(args.size() == 1){
				if(!(args.get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber start = (IntegerNumber) args.get(0);
				return new StringExpression(value.substring(Integer.parseInt(start.getValue())));
			}else if(args.size() == 2){
				if(!(args.get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber start = (IntegerNumber) args.get(0);
				IntegerNumber end = (IntegerNumber) args.get(1);
				int op1 = Integer.parseInt(start.getValue());
				int op2 = Integer.parseInt(end.getValue());
				return new StringExpression(value.substring(op1, op2));
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("charAt")){
			if(args.size() == 1){
				if(!(args.get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber pos = (IntegerNumber) args.get(0);
				return new CharExpression(value.charAt(Integer.parseInt(pos.getValue())));
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("locate")){
			if(!(args.size() == 2 || args.size() == 3)){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof StringExpression){
				StringExpression se = (StringExpression) args.get(0);
				re = new SimpleRegular(se.getValue());
			}else if(args.get(0) instanceof RegularExpression){
				re = (RegularExpression) args.get(0);
			}else{
				return new HiwiiException();
			}
			int pos = 0;
			if(args.get(1) instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) args.get(1);
				pos = Integer.parseInt(in.getValue());
			}else{
				return new HiwiiException();
			}
			boolean forward = true;
			if(args.size() == 3 ){
				if(args.get(2) instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) args.get(2);
					forward = EntityUtil.judge(jr);
				}else if(args.get(2) instanceof NullValue){
					
				}else{
					return new HiwiiException();
				}
			}
			int ret = re.locate(value, pos, forward);
			return new IntegerNumber(String.valueOf(ret));
		}else if(name.equals("count")){
			if(!(args.size() == 2 || args.size() == 3 || args.size() == 4)){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof StringExpression){
				StringExpression se = (StringExpression) args.get(0);
				re = new SimpleRegular(se.getValue());
			}else if(args.get(0) instanceof RegularExpression){
				re = (RegularExpression) args.get(0);
			}else{
				return new HiwiiException();
			}
			int pos = 0;
			if(args.get(1) instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) args.get(1);
				pos = Integer.parseInt(in.getValue());
			}else{
				return new HiwiiException();
			}
			boolean forward = true;
			if(args.size() == 3 ){
				if(args.get(2) instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) args.get(2);
					forward = EntityUtil.judge(jr);
				}else if(args.get(2) instanceof NullValue){
					
				}else{
					return new HiwiiException();
				}
			}
			boolean greedy = true;
			if(args.size() == 4 ){
				if(args.get(3) instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) args.get(2);
					greedy = EntityUtil.judge(jr);
				}else if(args.get(3) instanceof NullValue){
					
				}else{
					return new HiwiiException();
				}
			}
			int ret = re.count(value, pos, forward, greedy);
			return new IntegerNumber(String.valueOf(ret));
		}
		return null;
	}

	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		if(name.equals("GT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			int ret = value.compareTo(se.getValue());
			if(ret > 0){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("LT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			int ret = value.compareTo(se.getValue());
			if(ret < 0){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("GE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			int ret = value.compareTo(se.getValue());
			if(ret >= 0){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("LE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			int ret = value.compareTo(se.getValue());
			if(ret <= 0){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("EQ")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			if(value.equals(se.getValue())){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("NE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			if(!value.equals(se.getValue())){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		}else if(name.equals("match")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof RegularExpression){
				re = (RegularExpression) args.get(0);
			}else{
				return new HiwiiException();
			}
			boolean ret = re.match(value);
			return EntityUtil.decide(ret);
		}else if(name.equals("guide")){
			if(!(args.size() == 2 || args.size() == 3)){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof StringExpression){
				StringExpression se = (StringExpression) args.get(0);
				re = new SimpleRegular(se.getValue());
			}else if(args.get(0) instanceof RegularExpression){
				re = (RegularExpression) args.get(0);
			}else{
				return new HiwiiException();
			}
			int pos = 0;
			if(args.get(1) instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) args.get(1);
				pos = Integer.parseInt(in.getValue());
			}else{
				return new HiwiiException();
			}
			boolean forward = true;
			if(args.size() == 3 ){
				if(args.get(2) instanceof JudgmentResult){
					JudgmentResult jr = (JudgmentResult) args.get(2);
					forward = EntityUtil.judge(jr);
				}else if(args.get(2) instanceof NullValue){
					
				}else{
					return new HiwiiException();
				}
			}
			boolean ret = re.guide(value, pos, forward);
			return EntityUtil.decide(ret);
		}else if(name.equals("in")){
			if(!(args.size() == 1)){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof StringExpression){
				StringExpression se = (StringExpression) args.get(0);
				re = new SimpleRegular(se.getValue());
			}else if(args.get(0) instanceof RegularExpression){
				re = (RegularExpression) args.get(0);
			}else if(args.get(0) instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) args.get(0);
				re = SystemPattern.getRegular(ie.getName());
				if(re == null){
					return new HiwiiException();
				}
			}else{
				return new HiwiiException();
			}
			if(re.match(value)){
				return EntityUtil.decide(true);
			}
			return EntityUtil.decide(false);
		}

		return null;
	}


	@Override
	public Expression doMappingDecision(String name, List<Expression> args) {
		if(name.equals("in")){
			if(!(args.size() == 1)){
				return new HiwiiException();
			}
			RegularExpression re = null;
			if(args.get(0) instanceof StringExpression){
				StringExpression se = (StringExpression) args.get(0);
				re = new SimpleRegular(se.getValue());
			}else if(args.get(0) instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) args.get(0);
				re = SystemPattern.getRegular(ie.getName());
				if(re == null){
					return new HiwiiException();
				}
			}else{
				//TODD regular calculation
				return new HiwiiException();
			}
			if(re.match(value)){
				return EntityUtil.decide(true);
			}
			return EntityUtil.decide(false);
		}
		return null;
	}
	/**
	 * toBlock,toStatement,toExpression,toJudgment四个运算使用RuntimePool作为参数。
	 * 实际调用中不使用参数，格式如下：
	 * "string".toBlock()
	 * 因此，operation hub调用object.doOperation(id/function,RuntimePool)
	 * 传送RuntimePool的目的是如果运算使用pool作为参数，则由对象中实现。
	 * @param pool
	 * @return
	 */
	public Expression toBlock(){
		String src = value;
		ByteArrayInputStream stream = new ByteArrayInputStream(src.getBytes());
		SystemParser parser = new SystemParser(stream);
		BraceExpression prg = null;
		try {
			prg = parser.getProgram();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prg;
	}
	
	public Expression toStatement(RuntimePool pool){
		String src = value;
		ByteArrayInputStream stream = new ByteArrayInputStream(src.getBytes());
		SystemParser parser = new SystemParser(stream);
		Expression prg = null;
		try {
			prg = parser.getExpression();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prg;
	}
	
	public Expression toExpression(){
		String src = value;
		ByteArrayInputStream stream = new ByteArrayInputStream(src.getBytes());
		SystemParser parser = new SystemParser(stream);
		Expression prg = null;
		try {
			prg = parser.getExpression();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		}
		return prg;
	}
	
	public Expression toJudgment(RuntimePool pool){
		String src = value;
		ByteArrayInputStream stream = new ByteArrayInputStream(src.getBytes());
		SystemParser parser = new SystemParser(stream);
		Expression prg = null;
		try {
			prg = parser.getJudgment();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prg;
	}

	@Override
	public String getClassName() {
		return "String";
	}

	public boolean isDefinition(String def){
		Expression expr = SystemDefinition.doIsPositive("String", def);
		//TODO SystemDefinition.doIsPasitive应该返回boolean，definition是否Entity
		if(expr instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) expr;
			return jr.isResult();
		}else{
			return false;
		}			
	}
	
	/**
	 * 从pos位置取长度len的子串,包括pos位置。
	 * 如果len超出长度，则抛出异常
	 * forward：true向右，false向左
	 * @param pos
	 * @param len
	 * @param forward
	 * @return
	 */
	public String getSubstring(int pos, int len, boolean forward) throws ApplicationException{
		if(forward){
			if(pos + len > value.length()){
				throw new ApplicationException("position err!");
			}
			return value.substring(pos, pos + len);
		}else{
			if(pos - len < -1){
				throw new ApplicationException("position err!");
			}
			return value.substring(pos - len + 1, pos + 1);
		}
	}
}
