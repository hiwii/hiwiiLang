package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

public class EachLoop extends Expression {
	private String name; //±äÁ¿Ãû
	private Expression items;
	private Expression program;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getItems() {
		return items;
	}
	public void setItems(Expression items) {
		this.items = items;
	}
	public Expression getProgram() {
		return program;
	}
	public void setProgram(Expression program) {
		this.program = program;
	}
	
}
