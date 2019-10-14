package net.hiwii.expr;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;

public class FunctionExpression extends Expression {
	private String name;
	private List<Expression> arguments;

	public FunctionExpression(){
		
	}
	public FunctionExpression(String name, List<Expression> arguments) {
		this.name = name;
		this.arguments = arguments;
	}
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
	
	@Override
	public Expression getEnhancedArgument(List<Expression> args) {
		FunctionExpression fe = new FunctionExpression();
		fe.setName(name);
		List<Expression> fargs = new ArrayList<Expression>();
		for(Expression expr:arguments){
			Expression ret = expr.getEnhancedArgument(args);
			fargs.add(ret);
		}
		fe.setArguments(fargs);
		return fe;
	}
	
	@Override
	public String toString() {
		String ret = name + "(";
		int i = 0;
//		int size = arguments.size();
		for(Expression exp:arguments){
			if(i == 0)
				ret = ret + exp.toString();
			else// if(i == size - 1)
				ret = ret + "," + exp.toString();
			i++;
		}
		ret = ret + ")";
		return ret;
	}

}
