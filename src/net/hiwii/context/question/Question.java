package net.hiwii.context.question;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * 'a' ask question
 * 'w' whether question
 * 's' selection question
 * 't' what question,ÈçÑÕÉ«
 * @author Administrator
 *
 */
public class Question extends Entity {
	private char type;//
	private Expression expression;
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}
