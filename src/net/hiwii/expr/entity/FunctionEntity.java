package net.hiwii.expr.entity;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

public class FunctionEntity extends Expression {
	private String name;
	private List<Entity> arguments;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Entity> getArguments() {
		return arguments;
	}
	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
	
}
