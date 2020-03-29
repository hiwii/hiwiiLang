package net.hiwii.def.decl;

import java.util.List;

import net.hiwii.cognition.Expression;

public class MappingDeclaration {
//	private String type;
	private List<String> arguments;//²ÎÊıÃû
//	private List<Expression> argType;
	private Expression statement;

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public Expression getStatement() {
		return statement;
	}

	public void setStatement(Expression statement) {
		this.statement = statement;
	}

}
