package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class FunctionMapping extends Expression {
	private String name;
	private List<Expression> fargs;
	private List<Expression> margs;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Expression> getFargs() {
		return fargs;
	}
	public void setFargs(List<Expression> fargs) {
		this.fargs = fargs;
	}
	public List<Expression> getMargs() {
		return margs;
	}
	public void setMargs(List<Expression> margs) {
		this.margs = margs;
	}
	@Override
	public String toString() {
		String ret = name + "(";
		int i = 0;
		for(Expression exp:fargs){
			if(i == 0)
				ret = ret + exp.toString();
			else
				ret = ret + "," + exp.toString();
			i++;
		}
		ret = ret + ")[";
		i = 0;
		for(Expression exp:margs){
			if(i == 0)
				ret = ret + exp.toString();
			else
				ret = ret + "," + exp.toString();
			i++;
		}
		ret = ret + "]";
		return ret;
	}
	
}
