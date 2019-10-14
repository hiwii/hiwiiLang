package net.hiwii.system.syntax.number;

import java.math.MathContext;

import net.hiwii.message.HiwiiException;
import net.hiwii.view.Entity;

public class RealNumber extends NumberExpression {
	private char sign;

	public RealNumber() {
		sign = '+';
	}

	public char getSign() {
		return sign;
	}

	public void setSign(char sign) {
		this.sign = sign;
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("minus")){
			if(sign == '+'){
				sign = '-';
			}else{
				sign = '+';
			}
			return this;
		}else if(name.equals("plus")){
			return this;
		}else{
			return new HiwiiException();
		}
//		if(name.equals("Definition")){
//			return new IdentifierExpression(getDefinition());
//		}else{
//			return new HiwiiException();
//		}
	}

	@Override
	public String getClassName() {
		return "Number";
	}

	public RealNumber abs(){
		if(sign == '-'){
			sign = '+';
		}
		return this;
	}
	
	public RealNumber negate(){
		if(sign == '+'){
			sign = '-';
		}else{
			sign = '+';
		}
		return this;
	}
	
	/**
	 * 精确到整数
	 * @param mc TODO
	 * @return
	 */
	public IntegerNumber toInteger(MathContext mc){
		return new IntegerNumber("1");
	}
	
	public DecimalNumber round(MathContext mc){
		return new DecimalNumber("1.0");
	}

	@Override
	public boolean greatZero() {
		if(sign == '+'){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean lessZero() {
		if(sign == '+'){
			return false;
		}else{
			return true;
		}
	}
}
