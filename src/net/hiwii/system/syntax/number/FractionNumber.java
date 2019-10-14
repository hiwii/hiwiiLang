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
import net.hiwii.view.Entity;

public class FractionNumber extends RealNumber {
	private String numerator;
	private String denominator;
	public FractionNumber(String numerator, String denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		setSign('+');
		setClassName("Fraction");
	}
	public String getNumerator() {
		return numerator;
	}
	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}
	public String getDenominator() {
		return denominator;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
	
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		MathContext mc = null;
		boolean precise = true;
//		try {
//			HiwiiMathContext hmc = dealContext(adverbs);
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
				BigDecimal op1 = new BigDecimal(toString());
				int op2 = new BigInteger(in.toString()).intValue();
				BigDecimal res = op1.pow(op2);
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
	
	public RealNumber simply(){
		BigInteger op1 = new BigInteger(numerator);
		BigInteger op2 = new BigInteger(denominator);
		BigInteger g = op1.gcd(op2);
		BigInteger den = op2.divide(g);
		if(den.compareTo(new BigInteger("1")) == 0){
			return new IntegerNumber(op1.divide(g).toString());
		}
		FractionNumber fn = new FractionNumber(op1.divide(g).toString(), op2.divide(g).toString());
		return fn;
	}
	
	@Override
	public String toString() {
		if(getSign() == '-'){
			return getSign() + numerator + "/" + denominator;
		}else{
			return numerator + "/" + denominator;
		}
	}
	
	@Override
	public IntegerNumber toInteger(MathContext mc) {
		BigDecimal num = new BigDecimal(numerator);
		BigDecimal den = new BigDecimal(denominator);
		if(getSign() == '-'){
			num = num.negate();
		}
		BigDecimal res = num.divide(den, mc.getPrecision(), mc.getRoundingMode());
		return new IntegerNumber(res.toBigInteger().toString());
	}
	
	@Override
	public DecimalNumber round(MathContext mc) {
		BigDecimal num = new BigDecimal(numerator);
		BigDecimal den = new BigDecimal(denominator);
		BigDecimal res = num.divide(den, mc.getPrecision(), mc.getRoundingMode());
		if(getSign() == '-'){
			res = res.negate();
		}
		DecimalNumber fn = new DecimalNumber(res.toString());		
		return fn;
	}
	@Override
	public boolean equalZero() {
		BigInteger bi = new BigInteger(numerator);
		if(bi.equals(BigInteger.ZERO)){
			return true;
		}
		return false;
	}
}
