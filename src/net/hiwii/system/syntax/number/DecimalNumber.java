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
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.MathUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;


public class DecimalNumber extends RealNumber {
	private String left;
	private String right;
	
	public DecimalNumber(String value) {
		this.right = StringUtil.getPointRight(value);
		if(value.charAt(0) == '+' || value.charAt(0) == '-'){
			this.left = StringUtil.getPointLeft(value.substring(1));
			this.setSign(value.charAt(0));
		}else{
			this.left = StringUtil.getPointLeft(value);
			this.setSign('+');
		}
		setClassName("Float");
	}

	
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}	public void setRight(String right) {
		this.right = right;
	}

	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		MathContext mc = null;
		boolean precise = true;
		int precision = 0;
		boolean boolSet = false, valSet = false;

//		if(adverbs != null){
//			for(AdverbContext cxt:adverbs){
//				Expression bool = cxt.doIdentifierPositive("precise", null);
//				if(bool instanceof JudgmentResult){
//					JudgmentResult jr = (JudgmentResult) bool;
//					precise = EntityUtil.judgeResult(jr);
//					boolSet = true;
//					break;
//				}
//			}

//			for(AdverbContext cxt:adverbs){
//				Entity val = cxt.doIdentifierCalculation("precision", null);
//				if(val instanceof IntegerNumber){
//					IntegerNumber in = (IntegerNumber) val;
//					precision = EntityUtil.toNumber(in);
//					valSet = true;
//					break;
//				}
//			}
//		}

		if(!precise){
			mc = new MathContext(precision, RoundingMode.HALF_UP);
		}
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
				BigInteger bi = new BigInteger(right.getValue());
				int i = bi.intValue();
				if(i == 0){
					return new HiwiiException();
				}
				return MathUtil.divide(this, right);
			}else if(op instanceof DecimalNumber){
				DecimalNumber right = (DecimalNumber) op;
				BigInteger bi = new BigInteger(right.getLeft());
				int i = bi.intValue();
				if(i == 0){
					return new HiwiiException();
				}
				return MathUtil.divide(this, right);
			}else if(op instanceof FractionNumber){
				FractionNumber right = (FractionNumber) op;
				BigInteger bi = new BigInteger(right.getNumerator());
				int i = bi.intValue();
				if(i == 0){
					return new HiwiiException();
				}
				return MathUtil.divide(this, right);
			}else if(op instanceof ScientificNotation){
				ScientificNotation right = (ScientificNotation) op;
				BigDecimal bi = new BigDecimal(right.getValue());
				int i = bi.intValue();
				if(i == 0){
					return new HiwiiException();
				}
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
		}else if(name.equals("digitAt")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity op = args.get(0);
			if(op instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) op;
				int op2 = new BigInteger(in.toString()).intValue();
				if(op2 >= 0){
					if(op2 >= left.length()){
						return new IntegerNumber("0");
					}else{
						return new IntegerNumber("" + left.charAt(left.length() - op2 - 1));
					}
				}else{
					op2 = -op2; //abs op2
					if(op2 >= right.length()){
						return new IntegerNumber("0");
					}else{
						return new IntegerNumber("" + right.charAt(op2 - 1));
					}
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
	
	public DecimalNumber round(int precision){
		return round(precision, RoundingMode.HALF_UP);
	}
	public DecimalNumber round(int precision, RoundingMode rm){
		MathContext mc = new MathContext(precision, rm);
		String value = left + "." + right;
		BigDecimal bd = new BigDecimal(getSign() + value);
		value = bd.round(mc).toString();
		if(value.charAt(0) == '+' || value.charAt(0) == '-'){
			setSign(value.charAt(0));
			value = value.substring(1);
		}
		return this;
	}
	
	@Override
	public IntegerNumber toInteger(MathContext mc) {
		BigDecimal bd = new BigDecimal(toString(), mc);
		return new IntegerNumber(bd.toBigInteger().toString());
	}


	@Override
	public DecimalNumber round(MathContext mc) {
		BigDecimal bd = new BigDecimal(toString());	
		BigDecimal res = bd.setScale(mc.getPrecision(), mc.getRoundingMode());
		DecimalNumber fn = new DecimalNumber(res.toString());		
		return fn;
	}


	@Override
	public String toString() {
		String value = left + "." + right;
		if(getSign() == '-'){
			return getSign() + value;
		}else{
			return value;
		}
	}
	@Override
	public String getClassName() {
		return "Float";
	}
	
	@Override
	public String format(String arg) throws ApplicationException{
		try {
			String value = left + "." + right;
			double d = new BigDecimal(value).doubleValue();
			return String.format(arg, d);
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}
	
	
	@Override
	public boolean greatZero(){
		BigDecimal bi = new BigDecimal(this.toString());
		if(bi.compareTo(BigDecimal.ZERO) > 0){
			return true;
		}
		return false;
	}
	@Override
	public boolean lessZero(){
		BigDecimal bi = new BigDecimal(this.toString());
		if(bi.compareTo(BigDecimal.ZERO) < 0){
			return true;
		}
		return false;
	}
	@Override
	public boolean equalZero(){
		BigDecimal bi = new BigDecimal(this.toString());
		if(bi.compareTo(BigDecimal.ZERO) == 0){
			return true;
		}
		return false;
	}
}
