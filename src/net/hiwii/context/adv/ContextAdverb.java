package net.hiwii.context.adv;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.context.RuntimeContext;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

public class ContextAdverb extends Entity {
	private List<Expression> describes;
	private RuntimeContext context;
	
	public List<Expression> getDescribes() {
		return describes;
	}
	public void setDescribes(List<Expression> states) {
		this.describes = states;
	}
	public RuntimeContext getContext() {
		return context;
	}
	public void setContext(RuntimeContext context) {
		this.context = context;
	}
	
	
}
