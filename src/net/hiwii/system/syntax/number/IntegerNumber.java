package net.hiwii.system.syntax.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.context.AdverbContext;
import net.hiwii.def.Definition;
import net.hiwii.def.SimpleDefinition;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.MathUtil;
import net.hiwii.view.Entity;


public class IntegerNumber extends RealNumber {
	private String value;
	
	public IntegerNumber(String value) {
		if(value.charAt(0) == '-' || value.charAt(0) == '+'){
			this.value = value.substring(1);
			this.setSign(value.charAt(0));
		}else{
			this.value = value;
			this.setSign('+');
		}
		setClassName("Integer");
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		MathContext mc = null;
		boolean precise = true;
//		try {
//			HiwiiMathContext hmc = dealContext(adv);
//			if(hmc != null){
//				mc = new MathContext(hmc.getPrecision(), RoundingMode.HALF_UP);
//				precise = hmc.isPrecise();
//			}
//		} catch (ApplicationException e) {
//			return new HiwiiException();//"adverb exception!"
//		}
		if(mc == null){
			mc = MathUtil.getDefaultContext();
		}
		if(!precise){
			if(name.equals("plus")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity op = args.get(0);
				if(op instanceof IntegerNumber){
					IntegerNumber right = (IntegerNumber) op;
					return MathUtil.plus(this, right, mc);
				}else if(op instanceof DecimalNumber){
					DecimalNumber right = (DecimalNumber) op;
					return MathUtil.plus(this, right, mc);
				}else if(op instanceof FractionNumber){
					FractionNumber right = (FractionNumber) op;
					return MathUtil.plus(this, right, mc);
				}else if(op instanceof ScientificNotation){
					ScientificNotation right = (ScientificNotation) op;
					return MathUtil.plus(this, right, mc);
				}
			}else if(name.equals("minus")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity op = args.get(0);
				if(op instanceof IntegerNumber){
					IntegerNumber right = (IntegerNumber) op;
					return MathUtil.minus(this, right, mc);
				}else if(op instanceof DecimalNumber){
					DecimalNumber right = (DecimalNumber) op;
					return MathUtil.minus(this, right, mc);
				}else if(op instanceof FractionNumber){
					FractionNumber right = (FractionNumber) op;
					return MathUtil.minus(this, right, mc);
				}else if(op instanceof ScientificNotation){
					ScientificNotation right = (ScientificNotation) op;
					return MathUtil.minus(this, right, mc);
				}
			}else if(name.equals("multiply")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity op = args.get(0);
				if(op instanceof IntegerNumber){
					IntegerNumber right = (IntegerNumber) op;
					return MathUtil.multiply(this, right, mc);
				}else if(op instanceof DecimalNumber){
					DecimalNumber right = (DecimalNumber) op;
					return MathUtil.multiply(this, right, mc);
				}else if(op instanceof FractionNumber){
					FractionNumber right = (FractionNumber) op;
					return MathUtil.multiply(this, right, mc);
				}else if(op instanceof ScientificNotation){
					ScientificNotation right = (ScientificNotation) op;
					return MathUtil.multiply(this, right, mc);
				}
			}else if(name.equals("divide")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity op = args.get(0);
				if(!(op instanceof NumberExpression)){
					return new HiwiiException();
				}
				NumberExpression ne = (NumberExpression) op;
				if(ne.equalZero()){
					return new HiwiiException("zero divied error!");
				}
				if(op instanceof IntegerNumber){
					IntegerNumber right = (IntegerNumber) op;
					BigInteger bi = new BigInteger(right.getValue());
					int i = bi.intValue();
					if(i == 0){
						return new HiwiiException();
					}
					return MathUtil.divide(this, right, mc);
				}else if(op instanceof DecimalNumber){
					DecimalNumber right = (DecimalNumber) op;
					BigInteger bi = new BigInteger(right.getLeft());
					int i = bi.intValue();
					if(i == 0){
						return new HiwiiException();
					}
					return MathUtil.divide(this, right, mc);
				}else if(op instanceof FractionNumber){
					FractionNumber right = (FractionNumber) op;
					BigInteger bi = new BigInteger(right.getNumerator());
					int i = bi.intValue();
					if(i == 0){
						return new HiwiiException();
					}
					return MathUtil.divide(this, right, mc);
				}else if(op instanceof ScientificNotation){
					ScientificNotation right = (ScientificNotation) op;
					BigDecimal bi = new BigDecimal(right.getValue());
					int i = bi.intValue();
					if(i == 0){
						return new HiwiiException();
					}
					return MathUtil.divide(this, right, mc);
				}
			}else if(name.equals("pow")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity op = args.get(0);
				if(op instanceof IntegerNumber){
					IntegerNumber in = (IntegerNumber) op;
					if(in.getSign() == '-'){
						return null;
					}
					BigInteger op1 = new BigInteger(toString());
					int op2 = new BigInteger(in.toString()).intValue();
					BigInteger res = op1.pow(op2);
					return new IntegerNumber(res.toString());
				}else{
					return null;
				}
			}
		}
		if(name.equals("plus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				return MathUtil.plus(this, right);
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				return MathUtil.plus(this, right);
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				return MathUtil.plus(this, right);
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				return MathUtil.plus(this, right);
			}
		}else if(name.equals("minus")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				return MathUtil.minus(this, right);
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				return MathUtil.minus(this, right);
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				return MathUtil.minus(this, right);
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				return MathUtil.minus(this, right);
			}
		}else if(name.equals("multiply")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				return MathUtil.multiply(this, right);
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				return MathUtil.multiply(this, right);
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				return MathUtil.multiply(this, right);
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				return MathUtil.multiply(this, right);
			}
		}else if(name.equals("divide")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(!(op instanceof NumberExpression)){
				return new HiwiiException();
			}
			NumberExpression ne = (NumberExpression) op;
			if(ne.equalZero()){
				return new HiwiiException("zero divied error!");
			}
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				return MathUtil.divide(this, right);
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				return MathUtil.divide(this, right);
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				return MathUtil.divide(this, right);
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				return MathUtil.divide(this, right);
			}
		}else if(name.equals("pow")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				if(in.getSign() == '-'){
					return null;
				}
				BigInteger op1 = new BigInteger(toString());
				int op2 = new BigInteger(in.toString()).intValue();
				BigInteger res = op1.pow(op2);
				return new IntegerNumber(res.toString());
			}else{
				return null;
			}
		}else if(name.equals("mod")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				if(in.getSign() == '-'){
					return new HiwiiException();
				}
				BigInteger op1 = new BigInteger(toString());
				BigInteger op2 = new BigInteger(in.toString());
				BigInteger res = op1.mod(op2);
				return new IntegerNumber(res.toString());
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("digitAt")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				if(in.getSign() == '-'){
					return new IntegerNumber("0");
				}
				int op2 = new BigInteger(in.toString()).intValue();
				if(op2 >= value.length()){
					return new IntegerNumber("0");
				}else{
					return new IntegerNumber("" + value.charAt(value.length() - op2 - 1));
				}
			}else{
				return new HiwiiException();
			}
		}
		return null;
	}

	@Override
	public Expression doFunctionDecision(String name, List<Entity> args) {
		JudgmentResult jr = new JudgmentResult();
		if(name.equals("GT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.gt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.gt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.gt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.gt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}else if(name.equals("LT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.lt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.lt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.lt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.lt(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}else if(name.equals("GE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.ge(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.ge(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.ge(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.ge(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}else if(name.equals("LE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.le(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.le(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.le(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.le(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}else if(name.equals("EQ")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.eq(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.eq(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.eq(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.eq(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}else if(name.equals("NE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber right = (IntegerNumber) op;
				if(MathUtil.ne(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				if(MathUtil.ne(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				if(MathUtil.ne(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				if(MathUtil.ne(this, right)){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}
		}
		return null;
	}
	
	public boolean isDefinition(String def){
		Expression expr = SystemDefinition.doIsPositive("Integer", def);
		//TODO SystemDefinition.doIsPasitive应该返回boolean，definition是否Entity
		if(expr instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) expr;
			return jr.isResult();
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		if(getSign() == '-'){
			return getSign() + value;
		}else{
			return value;
		}
	}
	@Override
	public String getClassName() {
		return "Integer";
	}
	
	
	@Override
	public String format(String arg) throws ApplicationException{
		try {
			int d = Integer.parseInt(value);
			return String.format(arg, d);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}
	
	@Override
	public boolean greatZero(){
		BigInteger bi = new BigInteger(this.toString());
		if(bi.compareTo(BigInteger.ZERO) > 0){
			return true;
		}
		return false;
	}
	@Override
	public boolean lessZero(){
		BigInteger bi = new BigInteger(this.toString());
		if(bi.compareTo(BigInteger.ZERO) < 0){
			return true;
		}
		return false;
	}
	@Override
	public boolean equalZero(){
		BigInteger bi = new BigInteger(this.toString());
		if(bi.compareTo(BigInteger.ZERO) == 0){
			return true;
		}
		return false;
	}
	
}
