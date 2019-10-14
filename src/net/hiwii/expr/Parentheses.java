package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class Parentheses extends Expression {
	private List<Expression> array;

	public List<Expression> getArray() {
		return array;
	}

	public void setArray(List<Expression> array) {
		this.array = array;
	}
	@Override
	public String toString() {
		String ret = "(";
		for(Expression expr:array){
			ret = ret + expr.toString();
			if(!(expr == array.get(array.size() - 1))){
				ret = ret + ",";
			}
		}
		ret = ret + ")";
		return ret;
	}
	@Override
	public String getClassName() {
		return "Charactor";
	}
}
