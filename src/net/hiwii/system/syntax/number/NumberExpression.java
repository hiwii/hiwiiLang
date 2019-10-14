package net.hiwii.system.syntax.number;

import net.hiwii.cognition.Expression;
import net.hiwii.context.adv.ContextAdverb;
import net.hiwii.def.Definition;
import net.hiwii.def.SimpleDefinition;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class NumberExpression extends Expression {

	public NumberExpression() {
		super();
		setClassName("Number");
	}
	public boolean greatZero(){
		return true;
	}
	public boolean lessZero(){
		return false;
	}
	public boolean equalZero(){
		return false;
	}

}
