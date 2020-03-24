package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class FunctionMapping extends Expression {
	private String name;
	private List<Expression> arguments;
	private List<Expression> expressions;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Expression> getArguments() {
		return arguments;
	}
	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}
	public List<Expression> getExpressions() {
		return expressions;
	}
	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}
	@Override
	public String toString() {
		String ret = name + "(";
		int i = 0;
		for(Expression exp:arguments){
			if(i == 0)
				ret = ret + exp.toString();
			else
				ret = ret + "," + exp.toString();
			i++;
		}
		ret = ret + ")[";
		i = 0;
		for(Expression exp:expressions){
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
