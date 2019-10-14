package net.hiwii.expr;

import net.hiwii.cognition.Expression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.view.Entity;

public class NumberUnit extends Expression {
	private RealNumber number;
	private Expression unit;

	public RealNumber getNumber() {
		return number;
	}
	public void setNumber(RealNumber number) {
		this.number = number;
	}
	public Expression getUnit() {
		return unit;
	}
	public void setUnit(Expression unit) {
		this.unit = unit;
	}
	@Override
	public String toString() {		
		return number.toString() + unit.toString();
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("minus")){
			RealNumber num = number.negate();
			this.number = num;
			return this;
		}else if(name.equals("plus")){
			return this;
		}else{
			return new HiwiiException();
		}
	}
}
