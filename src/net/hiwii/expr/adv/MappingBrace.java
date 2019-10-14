package net.hiwii.expr.adv;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.MappingExpression;

public class MappingBrace extends Expression implements Adverb{
	private String name;
	private List<Expression> statements;
	private List<Expression> arguments;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public List<Expression> getStatements() {
		return statements;
	}
	@Override
	public void setStatements(List<Expression> statements) {
		this.statements = statements;
	}
	
	@Override
	public Expression getContent() {
		return new MappingExpression(name, arguments);
	}
	@Override
	public void setContent(Expression content) {
		MappingExpression fe = (MappingExpression) content;
		this.name = fe.getName();
		this.arguments = fe.getArguments();
	}
	
	public List<Expression> getArguments() {
		return arguments;
	}
	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}
}
