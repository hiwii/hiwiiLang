package net.hiwii.system.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.system.syntax.number.ScientificNotation;

public class MathUtil {
	public static boolean gt(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(IntegerNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger den = new BigInteger(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) > 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean gt(IntegerNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(DecimalNumber left, IntegerNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(DecimalNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal num = new BigDecimal(right.getNumerator());
		BigDecimal den = new BigDecimal(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) > 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean gt(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(FractionNumber left, IntegerNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(FractionNumber left, DecimalNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(FractionNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger num1 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(left.getDenominator());
		BigInteger num2 = new BigInteger(right.getNumerator());
		BigInteger den2 = new BigInteger(right.getDenominator());
		BigInteger op1 = num1.multiply(den2);
		BigInteger op2 = num2.multiply(den1);
		if(op1.compareTo(op2) > 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean gt(FractionNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.getNumerator());
		BigDecimal den1 = new BigDecimal(left.getDenominator());
		BigDecimal op2 = new BigDecimal(right.toString());
		
		op2 = op2.multiply(den1);
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean gt(ScientificNotation left, IntegerNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(ScientificNotation left, DecimalNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(ScientificNotation left, FractionNumber right){
		if(gt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean gt(ScientificNotation left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(IntegerNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return false;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return true;
		}
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger den = new BigInteger(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) < 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static boolean lt(IntegerNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return false;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return true;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(DecimalNumber left, IntegerNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(DecimalNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal num = new BigDecimal(right.getNumerator());
		BigDecimal den = new BigDecimal(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) < 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean lt(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(FractionNumber left, IntegerNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(FractionNumber left, DecimalNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(FractionNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger num1 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(left.getDenominator());
		BigInteger num2 = new BigInteger(right.getNumerator());
		BigInteger den2 = new BigInteger(right.getDenominator());
		BigInteger op1 = num1.multiply(den2);
		BigInteger op2 = num2.multiply(den1);
		if(op1.compareTo(op2) < 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean lt(FractionNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.getNumerator());
		BigDecimal den1 = new BigDecimal(left.getDenominator());
		BigDecimal op2 = new BigDecimal(right.toString());
		
		op2 = op2.multiply(den1);
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean lt(ScientificNotation left, IntegerNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(ScientificNotation left, DecimalNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(ScientificNotation left, FractionNumber right){
		if(lt(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean lt(ScientificNotation left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) < 0){
			return true;
		}else{
			return false;
		}
	}
	/*****/
	public static boolean le(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(IntegerNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return false;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return true;
		}
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger den = new BigInteger(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) <= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public static boolean le(IntegerNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return false;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return true;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(DecimalNumber left, IntegerNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(DecimalNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal num = new BigDecimal(right.getNumerator());
		BigDecimal den = new BigDecimal(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) <= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean le(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(FractionNumber left, IntegerNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(FractionNumber left, DecimalNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(FractionNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger num1 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(left.getDenominator());
		BigInteger num2 = new BigInteger(right.getNumerator());
		BigInteger den2 = new BigInteger(right.getDenominator());
		BigInteger op1 = num1.multiply(den2);
		BigInteger op2 = num2.multiply(den1);
		if(op1.compareTo(op2) <= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean le(FractionNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.getNumerator());
		BigDecimal den1 = new BigDecimal(left.getDenominator());
		BigDecimal op2 = new BigDecimal(right.toString());
		
		op2 = op2.multiply(den1);
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean le(ScientificNotation left, IntegerNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(ScientificNotation left, DecimalNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(ScientificNotation left, FractionNumber right){
		if(le(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean le(ScientificNotation left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) <= 0){
			return true;
		}else{
			return false;
		}
	}
	/******/
	/*****/
	public static boolean ge(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(IntegerNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger den = new BigInteger(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) >= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean ge(IntegerNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(DecimalNumber left, IntegerNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(DecimalNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal num = new BigDecimal(right.getNumerator());
		BigDecimal den = new BigDecimal(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) >= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean ge(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(FractionNumber left, IntegerNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(FractionNumber left, DecimalNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(FractionNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger num1 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(left.getDenominator());
		BigInteger num2 = new BigInteger(right.getNumerator());
		BigInteger den2 = new BigInteger(right.getDenominator());
		BigInteger op1 = num1.multiply(den2);
		BigInteger op2 = num2.multiply(den1);
		if(op1.compareTo(op2) >= 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean ge(FractionNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.getNumerator());
		BigDecimal den1 = new BigDecimal(left.getDenominator());
		BigDecimal op2 = new BigDecimal(right.toString());
		
		op2 = op2.multiply(den1);
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ge(ScientificNotation left, IntegerNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(ScientificNotation left, DecimalNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(ScientificNotation left, FractionNumber right){
		if(ge(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ge(ScientificNotation left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) >= 0){
			return true;
		}else{
			return false;
		}
	}
	/***********/
	/*****/
	public static boolean eq(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(IntegerNumber left, FractionNumber right){
		if(left.getSign() != right.getSign()){
			return false;
		}
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger den = new BigInteger(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(IntegerNumber left, ScientificNotation right){
		if(left.getSign() != right.getSign()){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(DecimalNumber left, IntegerNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(DecimalNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal num = new BigDecimal(right.getNumerator());
		BigDecimal den = new BigDecimal(right.getDenominator());
		op1 = op1.multiply(den);
		if(op1.compareTo(num) == 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean eq(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(FractionNumber left, IntegerNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(FractionNumber left, DecimalNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(FractionNumber left, FractionNumber right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigInteger num1 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(left.getDenominator());
		BigInteger num2 = new BigInteger(right.getNumerator());
		BigInteger den2 = new BigInteger(right.getDenominator());
		BigInteger op1 = num1.multiply(den2);
		BigInteger op2 = num2.multiply(den1);
		if(op1.compareTo(op2) == 0){
			if(left.getSign() == '+'){
				return true;
			}else{
				return false;
			}
		}else{
			if(left.getSign() == '-'){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public static boolean eq(FractionNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.getNumerator());
		BigDecimal den1 = new BigDecimal(left.getDenominator());
		BigDecimal op2 = new BigDecimal(right.toString());
		
		op2 = op2.multiply(den1);
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean eq(ScientificNotation left, IntegerNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(ScientificNotation left, DecimalNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(ScientificNotation left, FractionNumber right){
		if(eq(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean eq(ScientificNotation left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	/***********/
	/*****/
	public static boolean ne(IntegerNumber left, IntegerNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(IntegerNumber left, DecimalNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(IntegerNumber left, FractionNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(IntegerNumber left, ScientificNotation right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(DecimalNumber left, IntegerNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(DecimalNumber left, DecimalNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(DecimalNumber left, FractionNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(DecimalNumber left, ScientificNotation right){
		if(left.getSign() == '+' && right.getSign() == '-'){
			return true;
		}else if(left.getSign() == '-' && right.getSign() == '+'){
			return false;
		}
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		if(op1.compareTo(op2) == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean ne(FractionNumber left, IntegerNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(FractionNumber left, DecimalNumber right){
		if(ne(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ne(FractionNumber left, FractionNumber right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(FractionNumber left, ScientificNotation right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static boolean ne(ScientificNotation left, IntegerNumber right){
		if(ne(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ne(ScientificNotation left, DecimalNumber right){
		if(ne(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ne(ScientificNotation left, FractionNumber right){
		if(ne(right, left)){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean ne(ScientificNotation left, ScientificNotation right){
		if(eq(left, right)){
			return false;			
		}else{
			return true;
		}
	}
	
	public static RealNumber abs(RealNumber op){
		if(op.getSign() == '-'){
			op.setSign('+');
			return op;
		}else{
			return op;
		}
	}
	
	public static RealNumber negate(RealNumber op){
		if(op.getSign() == '-'){
			op.setSign('+');
			return op;
		}else{
			op.setSign('-');
			return op;
		}
	}
	
	/********************************/
	public static RealNumber plus(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		BigInteger res = op1.add(op2);
		return new IntegerNumber(res.toString());
	}
	
	public static RealNumber plus(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.add(op2);
		return new DecimalNumber(res.toString());
	}

	public static RealNumber plus(IntegerNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getValue());
		BigInteger den = new BigInteger(right.getDenominator());
		BigInteger num = new BigInteger(right.getNumerator());
		op0 = op0.multiply(den);
		BigInteger op01 = null;
		char sn;
		if(left.getSign() == right.getSign()){
			op01 = op0.add(num);
			sn = left.getSign();
		}else{
			op01 = op0.add(num.negate()).abs();
			if(op0.compareTo(num) > 0){
				sn = left.getSign();
			}else if(op0.compareTo(num) < 0){
				sn = right.getSign();
			}else{
				return new IntegerNumber("0");
			}
		}
		
		BigInteger g = op01.gcd(den);
		if(den.divide(g).compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(op01.toString());
			ret.setSign(sn);
			return ret;
		}
		FractionNumber fn = new FractionNumber(op01.divide(g).toString(), den.divide(g).toString());
		fn.setSign(sn);
		return fn;
	}
		
	public static RealNumber plus(IntegerNumber left, ScientificNotation right){
		BigInteger op0 = new BigInteger(left.getValue());
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return plus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return plus(left, val);
		}
	}

	/********************/
	public static RealNumber plus(DecimalNumber left, IntegerNumber right){
		return plus(right, left);
	}
	
	public static RealNumber plus(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.add(op2);
		if(res.compareTo(new BigDecimal("0")) == 0){
			return new IntegerNumber("0");
		}
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber plus(DecimalNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger op1 = new BigInteger("1" + StringUtil.fillChar(left.getRight().length(), '0'));
		BigInteger fd = new BigInteger(right.getDenominator());
		BigInteger f0 = new BigInteger(right.getNumerator());
		BigInteger denum = op1.multiply(fd);
		BigInteger num0 = op0.multiply(fd);
		BigInteger num1 = op1.multiply(f0);
		BigInteger num = null;
		char sn;
		if(left.getSign() == right.getSign()){					
			num = num0.add(num1);
			sn = left.getSign();
		}else{
			num = num0.add(num1.negate()).abs();
			if(num0.compareTo(num1) > 0){
				sn = left.getSign();
			}else if(num0.compareTo(num1) < 0){
				sn = right.getSign();
			}else{
				return new IntegerNumber("0");
			}
		}
		BigInteger g = denum.gcd(num);
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		if(sn == '-'){
			fn.setSign('-');
		}
		return fn;
	}
	
	public static RealNumber plus(DecimalNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return plus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return plus(left, val);
		}
	}
	
	/********************/
	public static RealNumber plus(FractionNumber left, IntegerNumber right){
		return plus(right, left);
	}
	
	public static RealNumber plus(FractionNumber left, DecimalNumber right){
		return plus(right, left);
	}
	
	public static RealNumber plus(FractionNumber left, FractionNumber right){
		BigInteger den0 = new BigInteger(left.getDenominator());
		BigInteger num0 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		BigInteger num1 = new BigInteger(right.getNumerator());
		
		BigInteger denum = null;
		BigInteger op0 = null;
		BigInteger op1 = null;
		if(den0.compareTo(den1) == 0){
			denum = den0;
			op0 = num0;
			op1 = num1;
		}else{
			denum = den0.multiply(den1);
			op0 = num0.multiply(den1);
			op1 = num1.multiply(den0);
		}

		
		BigInteger num = null;
		char sn;
		if(left.getSign() == right.getSign()){					
			num = op0.add(op1);
			sn = left.getSign();
		}else{
			num = op0.add(op1.negate()).abs();
			if(den0.compareTo(num1) > 0){
				sn = left.getSign();
			}else if(den0.compareTo(num1) < 0){
				sn = right.getSign();
			}else{
				return new IntegerNumber("0");
			}
		}
		BigInteger g = denum.gcd(num);
		if(denum.divide(g).compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.divide(g).toString());
			if(sn == '-'){
				ret.setSign('-');
			}
			return ret;
		}
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		if(sn == '-'){
			fn.setSign('-');
		}
		return fn;
	}
	
	public static RealNumber plus(FractionNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return plus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return plus(left, val);
		}
	}
	
	/********************/
	public static RealNumber plus(ScientificNotation left, IntegerNumber right){
		return plus(right, left);
	}
	
	public static RealNumber plus(ScientificNotation left, DecimalNumber right){
		return plus(right, left);
	}
		
	public static RealNumber plus(ScientificNotation left, FractionNumber right){
		return plus(right, left);
	}
	
	public static RealNumber plus(ScientificNotation left, ScientificNotation right){
		if(left.getPower().equals(right.getPower())){
			RealNumber ne = null;
			if(StringUtil.hasPoint(left.getValue())){
				DecimalNumber op0 = new DecimalNumber(left.getSign() + left.getValue());
				if(StringUtil.hasPoint(right.getValue())){
					DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
					ne = plus(op0, op1);
				}else{
					IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
					ne = plus(op0, op1);
				}
			}else{
				IntegerNumber op0 = new IntegerNumber(left.getSign() + left.getValue());
				if(StringUtil.hasPoint(right.getValue())){
					DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
					ne = plus(op0, op1);
				}else{
					IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
					ne = plus(op0, op1);
				}
			}
			ScientificNotation sn = new ScientificNotation(ne.toString(), left.getPower());
			//TODO value小于1，或>10情况
			return sn;
		}else{
			BigDecimal op1 = new BigDecimal(left.toString());
			BigDecimal op2 = new BigDecimal(right.toString());
			BigDecimal res = op1.add(op2);
			return new DecimalNumber(res.toString());
		}
	}
	/*************************/
	//precision为0表示精确到整数位，而java对应的是1
	public static RealNumber plus(IntegerNumber left, IntegerNumber right, MathContext mc){
		return plus(left, right);
	}
	
	public static RealNumber plus(IntegerNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return plus(left, fn2);
	}

	public static RealNumber plus(IntegerNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return plus(left, fn2);
	}
	public static RealNumber plus(IntegerNumber left, ScientificNotation right, MathContext mc){
		DecimalNumber sn = right.round(mc);
		return plus(left, sn);
	}
	public static RealNumber plus(DecimalNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn = right.toInteger(mc);
			return plus(fn, right);
		}
		DecimalNumber fn = left.round(mc);
		return plus(right, fn);
	}
	
	public static RealNumber plus(DecimalNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		BigDecimal bd1 = new BigDecimal(left.toString());
		BigDecimal bd2 = new BigDecimal(right.toString());
		BigDecimal res = bd1.add(bd2);
		res = res.setScale(mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	public static RealNumber plus(DecimalNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return plus(fn1, fn2);
	}
	
	public static RealNumber plus(DecimalNumber left, ScientificNotation right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			DecimalNumber fn2 = right.round(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return plus(fn1, fn2);
	}
	public static RealNumber plus(FractionNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			return plus(fn1, right);
		}
		DecimalNumber fn1 = left.round(mc);
		return plus(right, fn1);
	}	

	public static RealNumber plus(FractionNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return plus(fn1, fn2);
	}
	
	public static RealNumber plus(FractionNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return plus(fn1, fn2);
	}
	
	public static RealNumber plus(FractionNumber left, ScientificNotation right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			DecimalNumber fn2 = right.round(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return plus(fn1, fn2);
	}
	
	public static RealNumber plus(ScientificNotation left, IntegerNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		return plus(right, fn1);
	}
	
	public static RealNumber plus(ScientificNotation left, DecimalNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return plus(fn2, fn1);
	}
	
	public static RealNumber plus(ScientificNotation left, FractionNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return plus(fn1, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return plus(fn2, fn1);
	}
	
	public static RealNumber plus(ScientificNotation left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.add(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	/********************************/
	public static RealNumber minus(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		BigInteger res = op1.add(op2.negate());
		return new IntegerNumber(res.toString());
	}
	
	public static RealNumber minus(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.add(op2.negate());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber minus(IntegerNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getValue());
		BigInteger den = new BigInteger(right.getDenominator());
		BigInteger num = new BigInteger(right.getNumerator());
		op0 = op0.multiply(den);
		BigInteger op01 = null;
		char sn;
		if(left.getSign() != right.getSign()){
			op01 = op0.add(num);
			sn = left.getSign();
		}else{
			op01 = op0.add(num.negate()).abs();
			if(op0.compareTo(num) > 0){
				sn = left.getSign();
			}else if(op0.compareTo(num) < 0){
				sn = StringUtil.negate(left.getSign());
			}else{
				return new IntegerNumber("0");
			}
		}
		
		BigInteger g = op01.gcd(den);
		if(den.divide(g).compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(op01.toString());
			ret.setSign(sn);
			return ret;
		}
		FractionNumber fn = new FractionNumber(op01.divide(g).toString(), den.divide(g).toString());
		fn.setSign(sn);
		return fn;
	}
	
	public static RealNumber minus(IntegerNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return minus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return minus(left, val);
		}
	}
	
	/********************/
	public static RealNumber minus(DecimalNumber left, IntegerNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.add(op2.negate());
		if(res.compareTo(new BigDecimal("0")) == 0){
			return new IntegerNumber("0");
		}
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber minus(DecimalNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger op1 = new BigInteger("1" + StringUtil.fillChar(left.getRight().length(), '0'));
		BigInteger fd = new BigInteger(right.getDenominator());
		BigInteger f0 = new BigInteger(right.getNumerator());
		BigInteger denum = op1.multiply(fd);
		BigInteger num0 = op0.multiply(fd);
		BigInteger num1 = op1.multiply(f0);
		BigInteger num = null;
		char sn;
		if(left.getSign() != right.getSign()){					
			num = num0.add(num1);
			sn = left.getSign();
		}else{
			num = num0.add(num1.negate()).abs();
			if(num0.compareTo(num1) > 0){
				sn = left.getSign();
			}else if(num0.compareTo(num1) < 0){
				sn = StringUtil.negate(left.getSign());
			}else{
				return new IntegerNumber("0");
			}
		}
		BigInteger g = denum.gcd(num);
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		if(sn == '-'){
			fn.setSign('-');
		}
		return fn;
	}
	
	public static RealNumber minus(DecimalNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return minus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return minus(left, val);
		}
	}
	
	/********************/
	public static RealNumber minus(FractionNumber left, IntegerNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(FractionNumber left, DecimalNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(FractionNumber left, FractionNumber right){
		BigInteger den0 = new BigInteger(left.getDenominator());
		BigInteger num0 = new BigInteger(left.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		BigInteger num1 = new BigInteger(right.getNumerator());
		
		BigInteger denum = null;
		BigInteger op0 = null;
		BigInteger op1 = null;
		if(den0.compareTo(den1) == 0){
			denum = den0;
			op0 = num0;
			op1 = num1;
		}else{
			denum = den0.multiply(den1);
			op0 = num0.multiply(den1);
			op1 = num1.multiply(den0);
		}

		
		BigInteger num = null;
		char sn;
		if(left.getSign() != right.getSign()){					
			num = op0.add(op1);
			sn = left.getSign();
		}else{
			num = op0.add(op1.negate()).abs();
			if(den0.compareTo(num1) > 0){
				sn = left.getSign();
			}else if(den0.compareTo(num1) < 0){
				sn = StringUtil.negate(left.getSign());
			}else{
				return new IntegerNumber("0");
			}
		}
		BigInteger g = denum.gcd(num);
		if(denum.divide(g).compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.divide(g).toString());
			if(sn == '-'){
				ret.setSign('-');
			}
			return ret;
		}
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		if(sn == '-'){
			fn.setSign('-');
		}
		return fn;
	}
	
	public static RealNumber minus(FractionNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return minus(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return minus(left, val);
		}
	}
	
	/********************/
	public static RealNumber minus(ScientificNotation left, IntegerNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(ScientificNotation left, DecimalNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(ScientificNotation left, FractionNumber right){
		return negate(minus(right, left));
	}
	
	public static RealNumber minus(ScientificNotation left, ScientificNotation right){
		if(left.getPower().equals(right.getPower())){
			RealNumber ne = null;
			if(StringUtil.hasPoint(left.getValue())){
				DecimalNumber op0 = new DecimalNumber(left.getSign() + left.getValue());
				if(StringUtil.hasPoint(right.getValue())){
					DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
					ne = minus(op0, op1);
				}else{
					IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
					ne = minus(op0, op1);
				}
			}else{
				IntegerNumber op0 = new IntegerNumber(left.getSign() + left.getValue());
				if(StringUtil.hasPoint(right.getValue())){
					DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
					ne = minus(op0, op1);
				}else{
					IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
					ne = minus(op0, op1);
				}
			}
			ScientificNotation sn = new ScientificNotation(ne.toString(), left.getPower());
			//TODO value小于1，或>10情况
			return sn;
		}else{
			BigDecimal op1 = new BigDecimal(left.toString());
			BigDecimal op2 = new BigDecimal(right.toString());
			BigDecimal res = op1.add(op2.negate());
			return new DecimalNumber(res.toString());
		}
	}
	/*************************/
	public static RealNumber minus(IntegerNumber left, IntegerNumber right, MathContext mc){
		return minus(left, right);
	}
	
	public static RealNumber minus(IntegerNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return minus(left, fn2);
	}

	public static RealNumber minus(IntegerNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return minus(left, fn2);
	}
	public static RealNumber minus(IntegerNumber left, ScientificNotation right, MathContext mc){
		DecimalNumber sn = right.round(mc);
		return minus(left, sn);
	}
	public static RealNumber minus(DecimalNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn = left.toInteger(mc);
			return minus(fn, right);
		}
		DecimalNumber fn = left.round(mc);
		return minus(fn, right);
	}
	
	public static RealNumber minus(DecimalNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	public static RealNumber minus(DecimalNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(DecimalNumber left, ScientificNotation right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			DecimalNumber fn2 = right.round(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	public static RealNumber minus(FractionNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			return minus(fn1, right);
		}
		DecimalNumber fn1 = left.round(mc);
		return minus(fn1, right);
	}	

	public static RealNumber minus(FractionNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(FractionNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(FractionNumber left, ScientificNotation right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			DecimalNumber fn2 = right.round(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(ScientificNotation left, IntegerNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		return minus(fn1, right);
	}
	
	public static RealNumber minus(ScientificNotation left, DecimalNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(ScientificNotation left, FractionNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return minus(fn1, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return minus(fn1, fn2);
	}
	
	public static RealNumber minus(ScientificNotation left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.add(fn2.negate(), mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	/********************************/
	/********************************/
	public static RealNumber multiply(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.toString());
		BigInteger op2 = new BigInteger(right.toString());
		BigInteger res = op1.multiply(op2);
		return new IntegerNumber(res.toString());
	}
	
	public static RealNumber multiply(IntegerNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.multiply(op2);
		DecimalNumber fn = new DecimalNumber(res.toString());
		if(new BigInteger(fn.getRight()).equals(new BigInteger("0"))){
			return new IntegerNumber(fn.getSign() + fn.getLeft());
		}
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber multiply(IntegerNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getValue());
		BigInteger den = new BigInteger(right.getDenominator());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger g = op0.gcd(den);
		op0 = op0.divide(g);
		den = den.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){
			sn = '+';
		}else{
			sn = '-';
		}
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(op0.multiply(num).toString());
			ret.setSign(sn);
			return ret;
		}
		
		FractionNumber fn = new FractionNumber(op0.divide(g).toString(), den.divide(g).toString());
		fn.setSign(sn);
		return fn;
	}
	
	public static RealNumber multiply(IntegerNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return multiply(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return multiply(left, val);
		}
	}
	
	/********************/
	public static RealNumber multiply(DecimalNumber left, IntegerNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(DecimalNumber left, DecimalNumber right){
		BigDecimal op1 = new BigDecimal(left.toString());
		BigDecimal op2 = new BigDecimal(right.toString());
		BigDecimal res = op1.multiply(op2);
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber multiply(DecimalNumber left, FractionNumber right){
		BigInteger num0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger den0 = new BigInteger("1" + StringUtil.fillChar(left.getRight().length(), '0'));
		BigInteger num1 = new BigInteger(right.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(den1);
		BigInteger g1 = den0.gcd(num1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g1);
		BigInteger den10 = den1.divide(g0);
		
		BigInteger denum = den00.multiply(den10);
		BigInteger num = num00.multiply(num10);

		BigInteger g = denum.gcd(num);
		BigInteger den = denum.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.divide(g).toString());
			ret.setSign(sn);
			return ret;
		}
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		fn.setSign(sn);
		return fn;
	}
	
	public static RealNumber multiply(DecimalNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return multiply(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return multiply(left, val);
		}
	}
	
	/********************/
	public static RealNumber multiply(FractionNumber left, IntegerNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(FractionNumber left, DecimalNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(FractionNumber left, FractionNumber right){
		BigInteger num0 = new BigInteger(left.getNumerator());
		BigInteger den0 = new BigInteger(left.getDenominator());
		BigInteger num1 = new BigInteger(right.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(den1);
		BigInteger g1 = den0.gcd(num1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g1);
		BigInteger den10 = den1.divide(g0);
		
		BigInteger denum = den00.multiply(den10);
		BigInteger num = num00.multiply(num10);

		BigInteger g = denum.gcd(num);
		BigInteger den = denum.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.divide(g).toString());
			ret.setSign(sn);
			return ret;
		}
		FractionNumber fn = new FractionNumber(num.divide(g).toString(), denum.divide(g).toString());
		fn.setSign(sn);
		return fn;
	}
	
	public static RealNumber multiply(FractionNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return multiply(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return multiply(left, val);
		}
	}
	
	/********************/
	public static RealNumber multiply(ScientificNotation left, IntegerNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(ScientificNotation left, DecimalNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(ScientificNotation left, FractionNumber right){
		return multiply(right, left);
	}
	
	public static RealNumber multiply(ScientificNotation left, ScientificNotation right){
		RealNumber ne = null;
		IntegerNumber pow0 = new IntegerNumber(left.getPower());
		IntegerNumber pow1 = new IntegerNumber(right.getPower());
		if(StringUtil.hasPoint(left.getValue())){
			DecimalNumber op0 = new DecimalNumber(left.getSign() + left.getValue());
			if(StringUtil.hasPoint(right.getValue())){
				DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
				ne = multiply(op0, op1);
			}else{
				IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
				ne = multiply(op0, op1);
			}
		}else{
			IntegerNumber op0 = new IntegerNumber(left.getSign() + left.getValue());
			if(StringUtil.hasPoint(right.getValue())){
				DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
				ne = multiply(op0, op1);
			}else{
				IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
				ne = multiply(op0, op1);
			}
		}
		IntegerNumber power = (IntegerNumber) plus(pow0 ,pow1);
		ScientificNotation sn = new ScientificNotation(ne.toString(), power.toString());
		return sn;
	}
	/*************************/
	public static RealNumber multiply(IntegerNumber left, IntegerNumber right, MathContext mc){
		return multiply(left, right);
	}
	
	public static RealNumber multiply(IntegerNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return multiply(left, fn2);
	}

	public static RealNumber multiply(IntegerNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(left, fn2);
		}
		DecimalNumber fn2 = right.round(mc);
		return multiply(left, fn2);
	}
	public static RealNumber multiply(IntegerNumber left, ScientificNotation right, MathContext mc){
		DecimalNumber sn = right.round(mc);
		return multiply(left, sn);
	}
	public static RealNumber multiply(DecimalNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn = left.toInteger(mc);
			return multiply(fn, right);
		}
		DecimalNumber fn = left.round(mc);
		return multiply(fn, right);
	}
	
	public static RealNumber multiply(DecimalNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(fn1, fn2);
		}
//		DecimalNumber fn1 = left.round(mc);
//		DecimalNumber fn2 = right.round(mc);
		BigDecimal bd1 = new BigDecimal(left.toString());
		BigDecimal bd2 = new BigDecimal(right.toString());
		BigDecimal res = bd1.multiply(bd2);
		res = res.setScale(mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	public static RealNumber multiply(DecimalNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return multiply(fn1, fn2);
	}
	
	public static RealNumber multiply(DecimalNumber left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.multiply(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	public static RealNumber multiply(FractionNumber left, IntegerNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			return multiply(fn1, right);
		}
		DecimalNumber fn1 = left.round(mc);
		return multiply(fn1, right);
	}	

	public static RealNumber multiply(FractionNumber left, DecimalNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return multiply(fn1, fn2, mc);
	}
	
	public static RealNumber multiply(FractionNumber left, FractionNumber right, MathContext mc){
		if(mc.getPrecision() == 0){
			IntegerNumber fn1 = left.toInteger(mc);
			IntegerNumber fn2 = right.toInteger(mc);
			return multiply(fn1, fn2);
		}
		DecimalNumber fn1 = left.round(mc);
		DecimalNumber fn2 = right.round(mc);
		return multiply(fn1, fn2, mc);
	}
	
	public static RealNumber multiply(FractionNumber left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.multiply(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber multiply(ScientificNotation left, IntegerNumber right, MathContext mc){
		DecimalNumber fn1 = left.round(mc);
		return multiply(fn1, right);
	}
	
	public static RealNumber multiply(ScientificNotation left, DecimalNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.multiply(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber multiply(ScientificNotation left, FractionNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.multiply(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber multiply(ScientificNotation left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.multiply(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	/********************************/
	/********************************/
	public static RealNumber divide(IntegerNumber left, IntegerNumber right){
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger op2 = new BigInteger(right.getValue());
//		BigInteger g = op1.gcd(op2);
//		BigInteger den = op2.divide(g);
//		if(den.compareTo(new BigInteger("1")) == 0){
//			IntegerNumber ret = new IntegerNumber(op1.divide(g).toString());
//			if(left.getSign() != right.getSign()){
//				ret.setSign('-');
//			}
//			return ret;
//		}
		FractionNumber fn = new FractionNumber(op1.toString(), op2.toString());
		if(left.getSign() != right.getSign()){
			fn.setSign('-');
		}
		return degrade(fn);
	}
	
	public static RealNumber divide(IntegerNumber left, DecimalNumber right){
		BigInteger op1 = new BigInteger(left.getValue());
		BigInteger num =  new BigInteger("1" + StringUtil.fillChar(right.getRight().length(), '0'));
		BigInteger den = new BigInteger(right.getLeft() + right.getRight());
		op1 = op1.multiply(num);
		
//		BigInteger g = op1.gcd(den);
//		BigInteger deno = den.divide(g);
//		if(deno.compareTo(new BigInteger("1")) == 0){
//			IntegerNumber ret = new IntegerNumber(op1.divide(g).toString());
//			if(left.getSign() != right.getSign()){
//				ret.setSign('-');
//			}
//			return ret;
//		}
		FractionNumber ret = new FractionNumber(op1.toString(), den.toString());
		if(left.getSign() != right.getSign()){
			ret.setSign('-');
		}
		return degrade(ret);
	}
	
	public static RealNumber divide(IntegerNumber left, FractionNumber right){
		BigInteger op0 = new BigInteger(left.getValue());
		BigInteger den = new BigInteger(right.getDenominator());
		BigInteger num = new BigInteger(right.getNumerator());
		BigInteger g = op0.gcd(num);
		op0 = op0.divide(g);
		num = num.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){
			sn = '+';
		}else{
			sn = '-';
		}
		if(num.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(op0.multiply(den).toString());
			ret.setSign(sn);
			return ret;
		}
		
		FractionNumber fn = new FractionNumber(op0.multiply(den).toString(), num.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(IntegerNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return divide(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return divide(left, val);
		}
	}
	
	/********************/
	public static RealNumber divide(DecimalNumber left, IntegerNumber right){
		BigInteger num0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger den0 = new BigInteger("1" + StringUtil.fillChar(left.getRight().length(), '0'));
		BigInteger op0 = new BigInteger(right.getValue());

		//采用先约分形式计算
		BigInteger g0 = num0.gcd(op0);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = op0.divide(g0);
		
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		
		den00 = den00.multiply(den0);
		if(den00.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num00.toString());
			ret.setSign(sn);
			return ret;
		}

		FractionNumber fn = new FractionNumber(num00.toString(), den00.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(DecimalNumber left, DecimalNumber right){
		BigInteger num0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger num1 = new BigInteger(right.getLeft() + right.getRight());

		if(left.getRight().length() > right.getRight().length()){
			int len = left.getRight().length() - right.getRight().length();
			BigInteger d0 = new BigInteger("1" + StringUtil.fillChar(len, '0'));
			num1 = num1.multiply(d0);
		}else if(left.getRight().length() < right.getRight().length()){
			int len =  right.getRight().length() - left.getRight().length();
			BigInteger d0 = new BigInteger("1" + StringUtil.fillChar(len, '0'));
			num0 = num0.multiply(d0);
		}else{
			
		}
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
//		BigInteger g0 = num0.gcd(num1);
//		BigInteger d0 = num1.divide(g0);
//		if(d0.compareTo(new BigInteger("1")) == 0){
//			IntegerNumber ret = new IntegerNumber(num0.divide(g0).toString());
//			ret.setSign(sn);
//			return ret;
//		}
		FractionNumber fn = new FractionNumber(num0.toString(), num1.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(DecimalNumber left, FractionNumber right){
		BigInteger num0 = new BigInteger(left.getLeft() + left.getRight());
		BigInteger den0 = new BigInteger("1" + StringUtil.fillChar(left.getRight().length(), '0'));
		BigInteger num1 = new BigInteger(right.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(num1);
		BigInteger g1 = den0.gcd(den1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g0);
		BigInteger den10 = den1.divide(g1);
		
		BigInteger denum = den00.multiply(num10);
		BigInteger num = num00.multiply(den10);

		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), denum.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(DecimalNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return divide(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return divide(left, val);
		}
	}
	
	/********************/
	public static RealNumber divide(FractionNumber left, IntegerNumber right){
		BigInteger op0 = new BigInteger(right.getValue());
		BigInteger den = new BigInteger(left.getDenominator());
		BigInteger num = new BigInteger(left.getNumerator());
		BigInteger g = op0.gcd(num);
		op0 = op0.divide(g);
		num = num.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){
			sn = '+';
		}else{
			sn = '-';
		}
		den = den.multiply(op0);
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.toString());
			ret.setSign(sn);
			return ret;
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), den.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(FractionNumber left, DecimalNumber right){
		BigInteger num0 = new BigInteger(left.getNumerator());
		BigInteger den0 = new BigInteger(left.getDenominator());
		BigInteger num1 = new BigInteger(right.getLeft() + right.getRight());
		BigInteger den1 = new BigInteger("1" + StringUtil.fillChar(right.getRight().length(), '0'));
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(num1);
		BigInteger g1 = den0.gcd(den1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g0);
		BigInteger den10 = den1.divide(g1);
		
		BigInteger denum = den00.multiply(num10);
		BigInteger num = num00.multiply(den10);

		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}

		FractionNumber fn = new FractionNumber(num.toString(), denum.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(FractionNumber left, FractionNumber right){
		BigInteger num0 = new BigInteger(left.getNumerator());
		BigInteger den0 = new BigInteger(left.getDenominator());
		BigInteger num1 = new BigInteger(right.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(num1);
		BigInteger g1 = den0.gcd(den1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g0);
		BigInteger den10 = den1.divide(g1);
		
		BigInteger denum = den00.multiply(num10);
		BigInteger num = num00.divide(den10);
	
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), denum.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(FractionNumber left, ScientificNotation right){
		BigDecimal op1 = new BigDecimal(right.toString());
		if(op1.scale() <= 0){
			BigInteger mid = op1.toBigInteger();
			IntegerNumber val = null;
			val = new IntegerNumber(mid.toString());
			return divide(left, val);
		}else{
			DecimalNumber val = null;
			val = new DecimalNumber(op1.toPlainString());
			return divide(left, val);
		}
	}
	
	/********************/
	public static RealNumber divide(ScientificNotation left, IntegerNumber right){
		BigInteger op0 = new BigInteger(right.getValue());
		String point = left.toString();
		if(point.charAt(0) == '-'){
			point = point.substring(1);
		}
		DecimalNumber fnum = new DecimalNumber(point);
		BigInteger num = new BigInteger(fnum.getLeft() + fnum.getRight());
		BigInteger den = new BigInteger("1" + StringUtil.fillChar(fnum.getRight().length(), '0'));
		BigInteger g = op0.gcd(num);
		op0 = op0.divide(g);
		num = num.divide(g);
		
		char sn;
		if(left.getSign() == right.getSign()){
			sn = '+';
		}else{
			sn = '-';
		}
		den = den.multiply(op0);
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber ret = new IntegerNumber(num.toString());
			ret.setSign(sn);
			return ret;
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), den.toString());
		fn.setSign(sn);
		return fn;
	}
	
	public static RealNumber divide(ScientificNotation left, DecimalNumber right){
		String point = left.toString();
		if(point.charAt(0) == '-'){
			point = point.substring(1);
		}
		DecimalNumber fnum = new DecimalNumber(point);
		BigInteger num0 = new BigInteger(fnum.getLeft() + fnum.getRight());
		BigInteger den0 = new BigInteger("1" + StringUtil.fillChar(fnum.getRight().length(), '0'));
		BigInteger num1 = new BigInteger(right.getLeft() + right.getRight());
		BigInteger den1 = new BigInteger("1" + StringUtil.fillChar(right.getRight().length(), '0'));
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(num1);
		BigInteger g1 = den0.gcd(den1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g0);
		BigInteger den10 = den1.divide(g1);
		
		BigInteger denum = den00.multiply(num10);
		BigInteger num = num00.divide(den10);

		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), denum.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	public static RealNumber divide(ScientificNotation left, FractionNumber right){
		String point = left.toString();
		if(point.charAt(0) == '-'){
			point = point.substring(1);
		}
		DecimalNumber fnum = new DecimalNumber(point);
		BigInteger num0 = new BigInteger(fnum.getLeft() + fnum.getRight());
		BigInteger den0 = new BigInteger("1" + StringUtil.fillChar(fnum.getRight().length(), '0'));
		BigInteger num1 = new BigInteger(right.getNumerator());
		BigInteger den1 = new BigInteger(right.getDenominator());
		//采用先约分形式计算
		BigInteger g0 = num0.gcd(num1);
		BigInteger g1 = den0.gcd(den1);
		
		BigInteger num00 = num0.divide(g0);
		BigInteger den00 = den0.divide(g1);
		BigInteger num10 = num1.divide(g0);
		BigInteger den10 = den1.divide(g1);
		
		BigInteger denum = den00.multiply(num10);
		BigInteger num = num00.divide(den10);
		
		char sn;
		if(left.getSign() == right.getSign()){					
			sn = '+';
		}else{
			sn = '-';
		}
		
		FractionNumber fn = new FractionNumber(num.toString(), denum.toString());
		fn.setSign(sn);
		return degrade(fn);
	}
	
	/**
	 * 无法准确表示，如1e10 / 3e10 = 1/3 * 1e10
	 * @param left
	 * @param right
	 * @return
	 */
	public static RealNumber divide(ScientificNotation left, ScientificNotation right){
		RealNumber ne = null;
		IntegerNumber pow0 = new IntegerNumber(left.getPower());
		IntegerNumber pow1 = new IntegerNumber(right.getPower());
		if(StringUtil.hasPoint(left.getValue())){
			DecimalNumber op0 = new DecimalNumber(left.getSign() + left.getValue());
			if(StringUtil.hasPoint(right.getValue())){
				DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
				ne = divide(op0, op1);
			}else{
				IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
				ne = divide(op0, op1);
			}
		}else{
			IntegerNumber op0 = new IntegerNumber(left.getSign() + left.getValue());
			if(StringUtil.hasPoint(right.getValue())){
				DecimalNumber op1 = new DecimalNumber(right.getSign() + right.getValue());
				ne = divide(op0, op1);
			}else{
				IntegerNumber op1 = new IntegerNumber(right.getSign() + right.getValue());
				ne = divide(op0, op1);
			}
		}
		IntegerNumber power = (IntegerNumber) minus(pow0 ,pow1);
		if(power.getValue().equals("0")){
			return ne;
		}
		ScientificNotation mid = new ScientificNotation("1", power.toString());
		if(ne instanceof FractionNumber){
			FractionNumber fn = (FractionNumber) ne;
			return multiply(fn, mid);
		}else if(ne instanceof IntegerNumber){
			IntegerNumber in = (IntegerNumber) ne;
			return multiply(in, mid);
		}else if(ne instanceof DecimalNumber){
			DecimalNumber fn = (DecimalNumber) ne;
			return multiply(fn, mid);
		}else{
			ScientificNotation sn = (ScientificNotation) ne;
			return multiply(sn, mid);
		}
	}
	/*************************/
	public static RealNumber divide(IntegerNumber left, IntegerNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber divide(IntegerNumber left, DecimalNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}

	public static RealNumber divide(IntegerNumber left, FractionNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	public static RealNumber divide(IntegerNumber left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new DecimalNumber(res.toString());
	}
	public static RealNumber divide(DecimalNumber left, IntegerNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber divide(DecimalNumber left, DecimalNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	public static RealNumber divide(DecimalNumber left, FractionNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber divide(DecimalNumber left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new DecimalNumber(res.toString());
	}
	public static RealNumber divide(FractionNumber left, IntegerNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}	

	public static RealNumber divide(FractionNumber left, DecimalNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber divide(FractionNumber left, FractionNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		BigDecimal res = fn1.divide(fn2, mc.getPrecision(), mc.getRoundingMode());
		return new DecimalNumber(res.toString());
	}
	
	public static RealNumber divide(FractionNumber left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.divide(fn2, mc1);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber divide(ScientificNotation left, IntegerNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.divide(fn2, mc1);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber divide(ScientificNotation left, DecimalNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.round(mc).toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.divide(fn2, mc1);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber divide(ScientificNotation left, FractionNumber right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.round(mc).toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.divide(fn2, mc1);
		if(mc.getPrecision() == 0){
			return new IntegerNumber(res.toBigInteger().toString());
		}
		return new ScientificNotation(res.toEngineeringString());
	}
	
	public static RealNumber divide(ScientificNotation left, ScientificNotation right, MathContext mc){
		BigDecimal fn1 = new BigDecimal(left.toString());
		BigDecimal fn2 = new BigDecimal(right.toString());
		MathContext mc1 = new MathContext(mc.getPrecision() + 1, mc.getRoundingMode());
		BigDecimal res = fn1.divide(fn2, mc1);
		return new ScientificNotation(res.toEngineeringString());
	}
	/********************************/
	/**
	 * 约分
	 * @param fn
	 * @return
	 */
	public static RealNumber degrade(FractionNumber fn){
		BigInteger num = new BigInteger(fn.getNumerator());
		BigInteger den = new BigInteger(fn.getDenominator());
		BigInteger g = den.gcd(num);
		
		FractionNumber ret = new FractionNumber(num.divide(g).toString(), den.divide(g).toString());
		ret.setSign(fn.getSign());
		den = new BigInteger(ret.getDenominator());
		if(den.compareTo(new BigInteger("1")) == 0){
			IntegerNumber in = new IntegerNumber(num.divide(g).toString());
			in.setSign(ret.getSign());
			return in;
		}
		return ret;
	}
	
	public static MathContext getDefaultContext(){
		MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
		return mc;
	}
	
	public static BigDecimal transToFloat(String left, String right, char sign){
		String val = left + "." + right;
		BigDecimal bd = new BigDecimal(val);
		if(sign == '+'){
			return bd;
		}else{
			return bd.negate();
		}
	}
	
}
