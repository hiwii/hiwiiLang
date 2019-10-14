package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * doFormat:expr1.expr2
 * @author ha-wangzhenhai
 *
 */
public class DotFormatExpression extends Expression {
	private List<Expression> expressions;

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

}
