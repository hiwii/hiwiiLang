package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.system.syntax.number.IntegerNumber;

public class IdentifierExpression extends Expression{
	private String name;
	
	public IdentifierExpression() {
	}
	public IdentifierExpression(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Expression getEnhancedArgument(List<Expression> args) {
		if(name.equals("argumentCount")){
			return new IntegerNumber(String.valueOf(args.size()));
		}
		return this;
	}

	@Override
	public String toString() {
		return name;
	}

}
