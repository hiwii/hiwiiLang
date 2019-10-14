package net.hiwii.def.decl;

import java.util.List;

import net.hiwii.cognition.Expression;

public class FunctionDeclaration {
//	private String type;
	private List<String> arguments;//²ÎÊýÃû
	private List<Expression> argType;
	private Expression statement;

	
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public List<Expression> getArgType() {
		return argType;
	}

	public void setArgType(List<Expression> argType) {
		this.argType = argType;
	}

	public Expression getStatement() {
		return statement;
	}

	public void setStatement(Expression statement) {
		this.statement = statement;
	}

}
