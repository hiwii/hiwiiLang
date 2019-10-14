package net.hiwii.system.syntax.cog;

import net.hiwii.cognition.Expression;

/**
 * 认识类语句
 * format:Declare [persist] [base] [action|operation|judgment] declares [= expression]
 * base = definition/expression
 * =expression属于implement
 * @author Administrator
 *
 */
public class DeclareSentence extends CognitionAction {
	private char type;//j:judge,a:action,o:operation
	private Expression declare;
	private Expression content;
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}

	public Expression getDeclare() {
		return declare;
	}
	public void setDeclare(Expression name) {
		this.declare = name;
	}
	public Expression getContent() {
		return content;
	}
	public void setContent(Expression content) {
		this.content = content;
	}
	
}
