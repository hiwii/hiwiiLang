package net.hiwii.system.syntax.number;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.context.AdverbContext;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.MathUtil;
import net.hiwii.view.Entity;


public class ScientificNotation extends RealNumber {
	private String value;
	private String power;
	
	public ScientificNotation(String value) {
		String str = "";
		if(value.charAt(0) == '+' || value.charAt(0) == '-'){
			str = value.substring(1);
			this.setSign(value.charAt(0));
		}else{
			str = value;
			this.setSign('+');
		}
		str = str.toUpperCase();
		int pos = str.indexOf('E');//没考虑'E'处于首和尾的情况
		if(pos >0){
			this.value = str.substring(0, pos);
			this.power = str.substring(pos + 1);
		}else{
			this.value = str;
			this.power = "0";
		}
		tune();
	}
	
	public ScientificNotation(String value, String power) {
		this.power = power;
		if(value.charAt(0) == '+' || value.charAt(0) == '-'){
			this.value = value.substring(1);
			this.setSign(value.charAt(0));
		}else{
			this.value = value;
			this.setSign('+');
		}
		tune();
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
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
				IntegerNumber in = (IntegerNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(in.toString());
				if(op1.compareTo(op2) > 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) > 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() == '+' && fn.getSign() == '-'){
					jr.setResult(true);
					return jr;
				}else if(getSign() == '-' && fn.getSign() == '+'){
					jr.setResult(false);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) > 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}else if(name.equals("LT")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				BigInteger op1 = new BigInteger(toString());
				BigInteger op2 = new BigInteger(in.toString());
				if(op1.compareTo(op2) < 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) < 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() == '+' && fn.getSign() == '-'){
					jr.setResult(false);
					return jr;
				}else if(getSign() == '-' && fn.getSign() == '+'){
					jr.setResult(true);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) < 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}else if(name.equals("GE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				BigInteger op1 = new BigInteger(toString());
				BigInteger op2 = new BigInteger(in.toString());
				if(op1.compareTo(op2) >= 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) >= 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() == '+' && fn.getSign() == '-'){
					jr.setResult(true);
					return jr;
				}else if(getSign() == '-' && fn.getSign() == '+'){
					jr.setResult(false);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) >= 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}else if(name.equals("LE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(in.toString());
				if(op1.compareTo(op2) <= 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) <= 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() == '+' && fn.getSign() == '-'){
					jr.setResult(false);
					return jr;
				}else if(getSign() == '-' && fn.getSign() == '+'){
					jr.setResult(true);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) <= 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}else if(name.equals("EQ")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(in.toString());
				if(op1.compareTo(op2) == 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) == 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() != fn.getSign()){
					jr.setResult(false);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) == 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}else if(name.equals("NE")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(in.toString());
				if(op1.compareTo(op2) != 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof DecimalNumber){
				DecimalNumber fn = (DecimalNumber) op;
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal op2 = new BigDecimal(fn.toString());
				if(op1.compareTo(op2) != 0){
					jr.setResult(true);
					return jr;
				}else{
					jr.setResult(false);
					return jr;
				}
			}else if(op instanceof FractionNumber){
				FractionNumber fn = (FractionNumber) op;
				if(getSign() != fn.getSign()){
					jr.setResult(true);
					return jr;
				}
				BigDecimal op1 = new BigDecimal(toString());
				BigDecimal num = new BigDecimal(fn.getNumerator());
				BigDecimal den = new BigDecimal(fn.getDenominator());
				op1 = op1.multiply(den);
				if(op1.compareTo(num) != 0){
					if(getSign() == '+'){
						jr.setResult(true);
						return jr;
					}else{
						jr.setResult(false);
						return jr;
					}
				}else{
					if(getSign() == '-'){
						jr.setResult(false);
						return jr;
					}else{
						jr.setResult(true);
						return jr;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		if(getSign() == '-'){
			return getSign() + value + "E" + power;
		}else{
			return value + "E" + power;
		}
	}
	
	@Override
	public IntegerNumber toInteger(MathContext mc) {
		// TODO Auto-generated method stub
		return super.toInteger(mc);
	}
	/**
	 * 科学计数的round与其它数不同，round只作用于value部分。
	 * 对数的部分进行近似化
	 */
	@Override
	public DecimalNumber round(MathContext mc) {
		BigDecimal bd = new BigDecimal(value);
		BigDecimal res = bd.setScale(mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toPlainString());
	}
	
	/**
	 * if数值部分不处于[1,10)之间，调整.
	 * 由于bigDecimal运算后处于[1,999]之间，只考虑这种情况，不考虑小于1的情况。
	 */
	public void tune(){
		int pos = value.indexOf('.');
		String part = null;
		String all = null;
		if(pos >= 0){
			part = value.substring(0, pos);
			all = value.substring(0, pos) + value.substring(pos + 1);
		}else{
			part = value;
			all = value;
		}
		if(part.length() > 1){
			BigInteger bi = new BigInteger(power);
			int shift = part.length() - 1;
			
			value = all.charAt(0) + "." +  all.substring(1);
			power = bi.add(new BigInteger(String.valueOf(shift))).toString();
		}
	}
	
}
