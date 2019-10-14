package net.hiwii.expr;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;

public class BraceExpression extends Expression{
	private List<Expression> array;

	public BraceExpression(){
		array = new ArrayList<Expression>();
	}
	public List<Expression> getArray() {
		return array;
	}

	public void setArray(List<Expression> array) {
		this.array = array;
	}
	@Override
	public String toString() {
		String ret = "{  \r\n";
		int i = 0, last = array.size() -1;
		String tail = ",";
		for(Expression expr:array){
			if(i == last){
				tail = "";
			}
			ret = ret + "\t" + expr.toString() + tail + "\r\n";
		}
		ret = ret + "\r\n" + "}";
		return ret;
	}

}
