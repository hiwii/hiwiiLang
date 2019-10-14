package net.hiwii.expr.adv;

import java.util.List;

import net.hiwii.cognition.Expression;

public interface Adverb {
	public List<Expression> getStatements();
	public void setStatements(List<Expression> adv);
	public Expression getContent();
	public void setContent(Expression content);
}
