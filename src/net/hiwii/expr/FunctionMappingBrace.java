package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

public class FunctionMappingBrace extends Expression {
	private String name;
	private List<Expression> arguments;
	private List<Expression> expressions;
	private List<Expression> statements;
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
	public List<Expression> getStatements() {
		return statements;
	}
	public void setStatements(List<Expression> statements) {
		this.statements = statements;
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
		ret = ret + "{";
		i = 0;
		for(Expression exp:statements){
			if(i == 0)
				ret = ret + exp.toString();
			else
				ret = ret + "," + exp.toString();
			i++;
		}
		ret = ret + "}";
		return ret;
	}
}
